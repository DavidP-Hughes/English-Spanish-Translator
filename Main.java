import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

class Node {
    String english;
    String spanish;
    int height;
    Node left, right;

    Node(String english, String spanish) {
        this.english = english;
        this.spanish = spanish;
        height = 1;
    }
}

public class Main {
    Node root;
    int totalSteps;
    int totalWords;

    int height(Node node) {
        if (node == null)
            return 0;
        return node.height;
    }

    int max(int a, int b) {
        return (a > b) ? a : b;
    }

    Node rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;

        return x;
    }

    Node leftRotate(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        return y;
    }

    int getBalance(Node node) {
        if (node == null)
            return 0;
        return height(node.left) - height(node.right);
    }

    Node insert(Node node, String english, String spanish) {
        if (node == null)
            return (new Node(english, spanish));

        if (english.compareTo(node.english) < 0)
            node.left = insert(node.left, english, spanish);
        else if (english.compareTo(node.english) > 0)
            node.right = insert(node.right, english, spanish);
        else
            return node;

        node.height = 1 + max(height(node.left), height(node.right));

        int balance = getBalance(node);

        if (balance > 1 && english.compareTo(node.left.english) < 0)
            return rightRotate(node);

        if (balance < -1 && english.compareTo(node.right.english) > 0)
            return leftRotate(node);

        if (balance > 1 && english.compareTo(node.left.english) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && english.compareTo(node.right.english) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    String search(Node node, String english, int steps) {
        if (node == null)
            return "Not Found";
        steps++;
        if (english.compareTo(node.english) < 0)
            return search(node.left, english, steps);
        else if (english.compareTo(node.english) > 0)
            return search(node.right, english, steps);
        else {
            totalSteps += steps;
            totalWords++;
            return node.spanish;
        }
    }

    int treeHeight(Node node) {
        if (node == null)
            return 0;
        return Math.max(treeHeight(node.left), treeHeight(node.right)) + 1;
    }

    void printTreeHeight() {
        System.out.println("Tree Height: " + treeHeight(root));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Main tree = new Main();
        File file = new File();//directory for excel file containing english words with spanish equivalent

        try {
            Scanner scan = new Scanner(file);

            while (scan.hasNextLine()) {
                String[] parts = scan.nextLine().split(",");
                tree.root = tree.insert(tree.root, parts[0], parts[1]);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
        String englishText = sc.nextLine();
        String[] words = englishText.split(" ");
        StringBuilder spanishText = new StringBuilder();
        for (String word : words) {
            String translation = tree.search(tree.root, word.toLowerCase(), 0);
            spanishText.append(translation).append(" ");
        }
        System.out.println(spanishText);
        tree.printTreeHeight();
        if (tree.totalWords > 0) {
            double averageSteps = (double) tree.totalSteps / tree.totalWords;
            System.out.println("Average Steps " + averageSteps);
        } else {
            System.out.println("No words found.");
        }
    }
}
