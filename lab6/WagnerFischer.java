package skeleton;
public class WagnerFischer {
    private char[] s1;
    private char[] s2;

    public WagnerFischer(String s1, String s2) {
        this.s1 = s1.toLowerCase().toCharArray();
        this.s2 = s2.toLowerCase().toCharArray();
    }

    private int min(int a, int b, int c) {
        return Math.min(a, Math.min(b, c));
    }

    /**
     * Using Dynamic Programming, the Wagner-Fischer algorithm is able to 
     * calculate the edit distance between two strings.
     * @return edit distance between s1 and s2
     */
    public int getDistance() {
        int[][] dp = new int[s1.length + 1][s2.length + 1];
        for (int i = 0; i <= s1.length; dp[i][0] = i++);
        for (int j = 0; j <= s2.length; dp[0][j] = j++);

        for (int i = 1; i <= s1.length; i++) {
            for (int j = 1; j <= s2.length; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = min(dp[i - 1][j] + 1, dp[i][j - 1] + 1, 
                    		dp[i - 1][j - 1] + 1);
                }
            }
        }
        return dp[s1.length][s2.length];
    }

    public static void main(String[] args) {
        WagnerFischer wf = new WagnerFischer("Caffe Mocha", "coffee moka");
        System.out.println(wf.getDistance());
        wf = new WagnerFischer("Frappuccino", "fappiccino");
        System.out.println(wf.getDistance());
    }

}
