package com.ff.searchsphere.search;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SnippetExtractor {

    private static final int SNIPPET_LENGTH = 150;
    private static final int WINDOW_SIZE = 60;

    private record Match(int index, String term) {}

    public String extract(String content, List<String> rawQueryWords) {

        if (content == null || content.isBlank()) {
            return "";
        }

        String lowerContent = content.toLowerCase();

        List<Match> matches = findAllMatches(lowerContent, rawQueryWords);

        if (matches.isEmpty()) {
            return truncate(content);
        }

        int[] region = findBestRegion(matches);

        int start = Math.max(0, region[0] - (SNIPPET_LENGTH - (region[1] - region[0])) / 2);
        int end = Math.min(content.length(), start + SNIPPET_LENGTH);

        start = expandToWordStart(content, start);
        end = expandToWordEnd(content, end);

        String snippet = content.substring(start, end).trim();

        if (start > 0) {
            snippet = "..." + snippet;
        }
        if (end < content.length()) {
            snippet = snippet + "...";
        }

        return snippet;
    }

    private List<Match> findAllMatches(String lowerContent, List<String> rawQueryWords) {

        List<Match> matches = new ArrayList<>();

        for (String word : rawQueryWords) {

            String lowerWord = word.toLowerCase();
            int fromIndex = 0;

            while (true) {
                int index = lowerContent.indexOf(lowerWord, fromIndex);
                if (index == -1) {
                    break;
                }
                matches.add(new Match(index, lowerWord));
                fromIndex = index + lowerWord.length();
            }
        }

        matches.sort((a, b) -> Integer.compare(a.index(), b.index()));

        return matches;
    }

    // Slide a window across matches, scoring by how many DISTINCT query
    // terms appear in it, not raw match count. This prevents a region
    // with one repeated term (e.g. a nav menu full of "spring") from
    // beating a region that actually contains multiple different terms.
    private int[] findBestRegion(List<Match> matches) {

        int bestStart = matches.get(0).index();
        int bestEnd = matches.get(0).index();
        int bestDistinctCount = 1;

        int windowStartIdx = 0;

        for (int i = 0; i < matches.size(); i++) {

            while (matches.get(i).index() - matches.get(windowStartIdx).index() > WINDOW_SIZE) {
                windowStartIdx++;
            }

            Set<String> distinctTermsInWindow = new HashSet<>();
            for (int j = windowStartIdx; j <= i; j++) {
                distinctTermsInWindow.add(matches.get(j).term());
            }

            int distinctCount = distinctTermsInWindow.size();

            if (distinctCount > bestDistinctCount) {
                bestDistinctCount = distinctCount;
                bestStart = matches.get(windowStartIdx).index();
                bestEnd = matches.get(i).index();
            }
        }

        return new int[] { bestStart, bestEnd };
    }

    private int expandToWordStart(String content, int index) {

        while (index > 0 && !Character.isWhitespace(content.charAt(index - 1))) {
            index--;
        }

        return index;
    }

    private int expandToWordEnd(String content, int index) {

        while (index < content.length() && !Character.isWhitespace(content.charAt(index))) {
            index++;
        }

        return index;
    }

    private String truncate(String content) {

        if (content.length() <= SNIPPET_LENGTH) {
            return content;
        }

        int end = expandToWordEnd(content, SNIPPET_LENGTH);

        return content.substring(0, end).trim() + "...";
    }
}