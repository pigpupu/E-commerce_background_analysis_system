package com.example.businessanalysis.utils;


import java.util.Collections;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.nlpcn.commons.lang.index.MemoryIndex;
import org.nlpcn.commons.lang.pinyin.Pinyin;

/**
 * @ClassName SearchUtils
 * @Description 搜索匹配工具, copy的
 * @Author 74707
 * @Date 2022/11/8 22:48
 * @Version V1.0
 */
public class SearchUtils {
  /**
   * 实现简单文字搜索
   *
   * @param key      用于搜索的关键词
   * @param wordList 待搜索的词库
   * @return List<String>
   */
  public static List<String> search(String key, List<String> wordList) {
    MemoryIndex<String> memoryIndex = new MemoryIndex<String>();
    if (StringUtils.isBlank(key) || CollectionUtils.isEmpty(wordList)) {
      return Collections.EMPTY_LIST;
    }
    for (String word : wordList) {
      // 汉字转为完整拼音，如：中国 -- zhongguo
      String fullChar = StringUtils.join(Pinyin.pinyin(word), "");
      // 汉字转为拼音缩写，如：中国 -- zg
      String firstChar = StringUtils.join(Pinyin.firstChar(word), "");
      memoryIndex.addItem(word, word, fullChar, firstChar);
    }
    return memoryIndex.suggest(key);
  }
}
