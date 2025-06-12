package Univ.imgsearch_c2c_capstone.util;

import java.util.Arrays;

public class CosineSimilarityUtil {

    // JSON 형식의 임베딩 벡터를 파싱하고, 코사인 유사도를 계산
    public static double similarity(String embedding1, String embedding2) {
        double[] vec1 = parseVector(embedding1);
        double[] vec2 = parseVector(embedding2);
        return cosineSimilarity(vec1, vec2);
    }

    private static double[] parseVector(String jsonEmbedding) {
        // 문자열을 "," 기준으로 자르고 Double로 변환
        return Arrays.stream(jsonEmbedding.split(","))
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    private static double cosineSimilarity(double[] a, double[] b) {
        if (a.length != b.length) return 0;

        double dot = 0.0, normA = 0.0, normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += Math.pow(a[i], 2);
            normB += Math.pow(b[i], 2);
        }

        return (normA == 0 || normB == 0) ? 0 : dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
