public class UserData {
    public static User getDefault() {
        return new User("potato@yandex.ru", "12345", "John");
    }
    public static User getChangedUser() {
        return new User("tomato@yandex.ru", "54321", "Gray");
    }
}
