package task2_tcp.registration_items;

import java.nio.ByteBuffer;
import java.util.Date;

public class Registration {

    // порядок сериализации (размер сериализуемого обьекта всегда постоянный USERNAME_LENGTH + Long.BYTES + 3 * Integer.BYTES bytes):
    // date             -> 8 byte
    // operation        -> 4 byte
    // value            -> 4 byte
    // userName length  -> 4 byte
    // userName         -> userName length byte

    public static final int USERNAME_LENGTH = 100;
    public static final int BUFFER_SIZE = USERNAME_LENGTH + Long.BYTES + 3 * Integer.BYTES;

    private Date m_date;
    private String m_userName;

    private int m_value; // успешность выполнения регистрации (0 - успешно; -1 - неуспешно) или чилос отправленных сообщений в статистике
    private Operation m_operation;


    public Registration(Date date, String userName, Operation operation, int value) throws IllegalArgumentException {
        if (date == null || userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("invalid parameters of registration");
        }

        if(USERNAME_LENGTH < userName.length()) {
            throw new IllegalArgumentException("userName.length() != [1," + USERNAME_LENGTH + "]");
        }

        this.m_value = value;
        this.m_operation = operation;
        this.m_date = date;
        this.m_userName = userName;
    }

    public Registration(String userName, Operation operation) throws IllegalArgumentException {
        this(new Date(), userName, operation, 0);
    }


    public Date getDate() {
        return this.m_date;
    }

    public String getUserName() {
        return this.m_userName;
    }

    public Operation getOperation() {
        return this.m_operation;
    }

    public int getValue() {
        return this.m_value;
    }


    public void setDate(Date date) throws NullPointerException {
        if (date == null) {
            throw new NullPointerException("date == null");
        }
        this.m_date = date;
    }

    public void setUserName(String userName) {
        if(userName == null) {
            throw new NullPointerException("userName == null");
        }

        if (USERNAME_LENGTH < userName.length()) {
            throw new IllegalArgumentException("userName.length() != [1," + USERNAME_LENGTH + "]");
        }
        this.m_userName = userName;
    }

    public void setOperation(Operation operation) {
        this.m_operation = operation;
    }

    public void setValue(int value) {
        this.m_value = value;
    }


    public static byte[] serialize(Registration registration) throws NullPointerException {
        if (registration != null) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            buffer.putLong(registration.m_date.getTime());
            buffer.putInt(registration.m_operation.GetIndex());
            buffer.putInt(registration.m_value);
            buffer.putInt(registration.m_userName.length());
            buffer.put(registration.m_userName.getBytes());
            buffer.put(new byte[USERNAME_LENGTH - registration.m_userName.length()]);
            return buffer.array();
        }
        else{
            throw new NullPointerException("registration == null");
        }
    }

    public static Registration deserialize(byte[] bArray) throws NullPointerException, IllegalArgumentException {
        if (bArray != null) {
            if (bArray.length == BUFFER_SIZE) {
                ByteBuffer buffer = ByteBuffer.wrap(bArray);

                Date date = new Date(buffer.getLong());

                Operation operation = Operation.values()[buffer.getInt()];

                int code = buffer.getInt();

                int unLength = buffer.getInt();
                if (!(0< unLength && unLength <= USERNAME_LENGTH)) {
                    throw new IllegalArgumentException("username length != [1, " + USERNAME_LENGTH + "]");
                }
                byte[] bUserName = new byte[unLength];
                buffer.get(bUserName);
                String userName = new String(bUserName);

                return new Registration(date, userName, operation, code);
            }
            else {
                throw new IllegalArgumentException("bArray is not registration");
            }
        }
        else {
         throw new NullPointerException("bArray == null");
        }
    }

    
    @Override
    public String toString() {
        return "[date = " + m_date.toString() + ", user = '" + m_userName +"']";
    }
}