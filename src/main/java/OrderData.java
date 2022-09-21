public class OrderData {
    public static Order getDefault() {
        return new Order(new String[] {"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa77"});
    }
    public static Order getInvalidIngrefients() {
        return new Order(new String[] {"61c0c5a71bdaaa6d", "1f82001bdaaa6f", "61c0c5a71d1f82"});
    }
    public static Order getEmptyOrder() {
        return new Order(new String[] {});
    }
}
