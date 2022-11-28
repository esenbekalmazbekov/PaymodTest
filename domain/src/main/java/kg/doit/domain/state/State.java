package kg.doit.domain.state;

public class State {
    public static class Success<T> extends State {
        private final T value;
        public Success(T value) {
            this.value = value;
        }
        public T getValue() {
            return value;
        }
    }

    public static class Loading extends State {
        private final boolean isLoading;

        public Loading(boolean isLoading) {
            this.isLoading = isLoading;
        }

        public boolean isLoading() {
            return isLoading;
        }
    }

    public static class Error extends State {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
