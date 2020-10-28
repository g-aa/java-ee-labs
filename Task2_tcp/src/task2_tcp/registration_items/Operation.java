package task2_tcp.registration_items;

public enum Operation {

    REGISTER(0),
    GET_STATISTICS(1),
    QUIT(2);

    private final int m_index;

    Operation(int index) {
        this.m_index = index;
    }

    public  int GetIndex() {
        return this.m_index;
    }
}