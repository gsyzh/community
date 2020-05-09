package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gsyzh
 * @create 2020-05-09 9:44
 */
@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                //添加前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词失败" + e.getMessage());
        }
        //将一个敏感词添加到前缀树中

    }

    private void addKeyword(String keyword) {
        TrieNode trieNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = trieNode.getSubNode(c);
            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                trieNode.addSubNode(c, subNode);
            }
            //指向子节点，进入下一轮循环
            trieNode = subNode;
            //设置结束标识
            if (i == keyword.length() - 1) {
                trieNode.setKeywordEnd(true);
            }
        }
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();
        while (position < text.length()) {
            char c = text.charAt(position);
            //跳过符号
            if (getSymbol(c)) {
                //如果指针1处于根节点，此符号计入结果
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                //无论符号在开头或者中间，指针3都向下走一步
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.getKeywordEnd()) {
                //发现敏感词，将字符串替换掉
                sb.append(REPLACEMENT);
                //进入下一个位置
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            } else {
                //继续检查下一个字符
                position++;
            }

        }
        //将最后一批字符计入到结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean getSymbol(Character c) {
        //此范围为东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private class TrieNode {
        //关键词结束的标识
        private boolean KeywordEnd = false;
        //子节点(key是下级字符,value是下级节点)
        private Map<Character, TrieNode> subNode = new HashMap<>();

        public void addSubNode(Character c, TrieNode node) {
            subNode.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNode.get(c);
        }

        public boolean getKeywordEnd() {
            return KeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            KeywordEnd = keywordEnd;
        }
    }
}
