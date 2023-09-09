public class RandomWord {
    public static void main(String[] args) {
        String champion = "";
        for (int i = 1; i <= args.length; i++) {
            if (Math.random() <= 1/i) {
                champion = args[i-1];
            }
        }
        System.out.println(champion);
    }
}

