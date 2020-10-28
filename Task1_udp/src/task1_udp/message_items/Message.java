package task1_udp.message_items;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Date;

public class Message {

    // порядок сериализации (размер сериализуемого обьекта всегда постоянный 24 + message length bytes):
    // id               -> 4 byte - не сереализуется (имет смысл только на серверной стороне)
    // command          -> 4 byte
    // date             -> 8 byte
    // address size     -> 4 byte
    // address          -> address size byte (4 byte)
    // port             -> 4 byte - не сереализуется
    // message length   -> 4 byte
    // message          -> message length byte (min 0 byte)
    // 24 bytes - все основные параметры класса без учета текста самого сообщения.

    public static final int MAX_MESSAGE_LENGTH = 100;
    public static final int BUFFER_SIZE = MAX_MESSAGE_LENGTH + 24;

    private static int last_id;

    private int m_id;
    private Command m_command;
    private String m_message;
    private Date m_date;
    private InetAddress m_address;
    private int m_port;


    public Message(Command command, String message, Date date, InetAddress address, int port) throws NullPointerException, IllegalArgumentException {
        if (message != null && date != null && address != null ) {
            if (message.length() <= MAX_MESSAGE_LENGTH) {
                this.m_id = last_id;
                this.m_command = command;
                this.m_message = message;
                this.m_date = date;
                this.m_address = address;
                this.m_port = port;
                last_id++;
            }
            else {
                throw new IllegalArgumentException("message length != [0," + Message.MAX_MESSAGE_LENGTH + "]!");
            }
        }
        else {
            throw new NullPointerException("Inputs argument (message, date, address) is null!");
        }
    }

    public Message(Command command, String message) throws IOException, NullPointerException {
        this(command, message, new Date(), InetAddress.getLocalHost(), 0);
    }

    public Message() throws IOException, NullPointerException {
        this(Command.PING, "", new Date(), InetAddress.getLocalHost(), 0);
    }

    public int GetLastId() {
        return last_id;
    }

    public int GetId() {
        return this.m_id;
    }

    public Command GetCommand() {
        return this.m_command;
    }

    public String GetMessage() {
        return this.m_message;
    }

    public Date GetDate() {
        return this.m_date;
    }

    public InetAddress GetAddress() {
        return this.m_address;
    }

    public int GetPort() {
        return this.m_port;
    }


    public void SetCommand(Command command) {
        this.m_command = command;
    }

    public void SetMessage(String message) throws NullPointerException {
        if(message == null) {
            throw  new NullPointerException("input string == null!");
        }
        this.m_message = message;
    }

    public void SetDate(long date) {
        this.m_date = new Date(date);
    }

    public void SetAddress(InetAddress address) {
        this.m_address = address;
    }

    public void SetPort(int port) {
        this.m_port = port;
    }


    public static  byte[] Serialize(Message message) throws NullPointerException {
        if (message != null) {
            int mSize = message.m_message.getBytes().length;
            int aSize = message.m_address.getAddress().length;

            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.putInt(message.m_command.ordinal());
            buffer.putLong(message.m_date.getTime());
            buffer.putInt(aSize);
            buffer.put(message.m_address.getAddress());
            buffer.putInt(mSize);
            buffer.put(message.m_message.getBytes());
            buffer.put(new byte[BUFFER_SIZE - (3 * Integer.BYTES + Long.BYTES + mSize + aSize)]);

            return buffer.array();
        }
        throw new NullPointerException("input argument - message == null!");
    }

    public static Message Deserialize(byte[] bArray) throws NullPointerException, IllegalArgumentException {
        if(bArray != null) {
            if(bArray.length == BUFFER_SIZE) {
                ByteBuffer buffer = ByteBuffer.wrap(bArray);

                Command command = Command.values()[buffer.getInt()];

                Date date = new Date(buffer.getLong());

                int aSize = buffer.getInt();
                if (aSize != 4) {
                    throw  new IllegalArgumentException("address size != 4!");
                }
                byte[] abArray = new byte[aSize];
                buffer.get(abArray);
                try {
                    InetAddress address = InetAddress.getByAddress(abArray);

                    int mSize = buffer.getInt();
                    if (!(0 <= mSize && mSize <= MAX_MESSAGE_LENGTH)) {
                        throw new IllegalArgumentException("message length != [0," + MAX_MESSAGE_LENGTH + "]!");
                    }
                    byte[] mbArray = new byte[mSize];
                    buffer.get(mbArray);
                    String message = new String(mbArray);

                    return new Message(command, message, date, address, 0);
                }
                catch (UnknownHostException e) {
                    throw new IllegalArgumentException("unknown host!");
                }
            }
            else {
                throw  new IllegalArgumentException("input byte array is not message!");
            }
        }
        else {
            throw new NullPointerException("input byte array == null!");
        }
    }


    @Override
    public String toString() {
        return "Message {" +
                "id = " + m_id +
                ", command = " + m_command +
                ", message = '" + m_message + "'" +
                ", date = " + m_date +
                ", address = " + m_address +
                ", port = " + m_port +
                '}';
    }
}