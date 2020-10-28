package task1_udp.message_items;

public enum Command {

    ADD(0),
    TOP_MESSAGE(1),
    CLIENT_MASSAGE(2),
    PING(3),
    QUIT(4);

    public final int m_value;

    Command(int value) {
        this.m_value = value;
    }

    public int GetValue() {
        return m_value;
    }
}