public class ProductSystem 
{
    private static class Product {
        String id;
        String name;
        String category;
        double price;
        
        Product(String id, String name, String category, double price) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
        }
        
        public String toString() {
            return String.format("Product ID: %s\nName: %s\nCategory: %s\nPrice: $%.2f", id, name, category, price);
        }
    }

    private static class Node {
        Product data;
        Node parent, left, right;
        boolean isRed;
        
        Node(Product data) {
            this.data = data;
            this.isRed = true;
            this.left = this.right = this.parent = null;
        }
    }

    private Node root;
    private final Node placeholder;
    
    public ProductSystem() {
        placeholder = new Node(null);
        placeholder.isRed = false;
        root = placeholder;
    }
    
    public void insert(String id, String name, String category, double price) {
        try {
            if (search(id) != null) {
                throw new IllegalArgumentException("Error: Product with ID " + id + " already exists!");
            }
            
            Node node = new Node(new Product(id, name, category, price));
            node.left = node.right = placeholder;
            
            Node y = null;
            Node x = root;
            
            while (x != placeholder) {
                y = x;
                x = (id.compareTo(x.data.id) < 0) ? x.left : x.right;
            }
            
            node.parent = y;
            if (y == null) root = node;
            else if (id.compareTo(y.data.id) < 0) y.left = node;
            else y.right = node;
                
            fixInsert(node);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private void fixInsert(Node k) {
        Node u;
        while (k.parent != null && k.parent.isRed) {
            if (k.parent == k.parent.parent.right) {
                u = k.parent.parent.left;
                if (u.isRed) {
                    u.isRed = false;
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.left) {
                        k = k.parent;
                        rightRotate(k);
                    }
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    leftRotate(k.parent.parent);
                }
            } else {
                u = k.parent.parent.right;
                if (u.isRed) {
                    u.isRed = false;
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    k = k.parent.parent;
                } else {
                    if (k == k.parent.right) {
                        k = k.parent;
                        leftRotate(k);
                    }
                    k.parent.isRed = false;
                    k.parent.parent.isRed = true;
                    rightRotate(k.parent.parent);
                }
            }
            if (k == root) break;
        }
        root.isRed = false;
    }
    
    private void leftRotate(Node x) {
        Node y = x.right;
        x.right = y.left;
        if (y.left != placeholder) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }
    
    private void rightRotate(Node y) {
        Node x = y.left;
        y.left = x.right;
        if (x.right != placeholder) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.right) y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }
    
    public Product search(String id) {
        Node result = searchNode(root, id);
        return (result != placeholder && result != null) ? result.data : null;
    }
    
    private Node searchNode(Node node, String id) {
        if (node == placeholder || node == null) return null;
        int comparable = id.compareTo(node.data.id);
        if (comparable == 0) return node;
        if (comparable < 0) return searchNode(node.left, id);
        return searchNode(node.right, id);
    }
    
    public static void main(String[] args) 
    {
        ProductSystem system = new ProductSystem();
        
        system.insert("40d32e8dd", "Beatrix Kiddo", "Family Planning & Activities", 10.36);
        system.insert("8810d7ffdc", "Shawn White", "Sports & Games", 3.06);
        system.insert("26175eb50", "Aigis", "Security Services", 12.12);
        
        System.out.println("Testing duplicate insertion:");
        system.insert("40d32e8dd", "Duplicate", "Test", 0.0);
        
        System.out.println("\nSearch Results:");
        
        Product found1 = system.search("40d32e8dd");
        System.out.println("\nSearch 1:");
        System.out.println(found1 != null ? found1 : "Product not found");
        
        Product found2 = system.search("8810d7ffdc");
        System.out.println("\nSearch 2:");
        System.out.println(found2 != null ? found2 : "Product not found");
        
        Product found3 = system.search("nonexistent");
        System.out.println("\nSearch 3:");
        System.out.println(found3 != null ? found3 : "Product not found");
    }
}