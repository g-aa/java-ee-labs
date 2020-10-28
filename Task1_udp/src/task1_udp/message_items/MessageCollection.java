package task1_udp.message_items;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class MessageCollection {

    private List<Message> m_messages;

    public  MessageCollection() {
        m_messages = new ArrayList<Message>();
    }


    public boolean AddNewMessage(Message message) throws NullPointerException {
        if (message != null) {
            if (m_messages.contains(message)) {
                return false;
            }
            m_messages.add(new Message(message.GetCommand(), message.GetMessage(), message.GetDate(), message.GetAddress(), message.GetPort()));
            return true;
        }
        else {
            throw new NullPointerException("input message == null!");
        }
    }

    public List<Message> GetTopMessage(int quantity) throws IllegalArgumentException {
        if (0 < quantity) {
            if (m_messages.size() < quantity) {
                return new ArrayList<Message>(m_messages);
            }
            return new ArrayList<Message>(m_messages.subList(0, quantity));
        }
        else {
            throw new IllegalArgumentException("input quantity < 1!");
        }
    }


    public List<Message> GetTopClientMessage(int quantity, InetAddress address) throws  NullPointerException, IllegalArgumentException {
        if(address != null) {
            if (0 < quantity) {
                List<Message> result = new ArrayList<Message>();
                for (Message message : m_messages) {
                    if (message.GetAddress().toString().equals(address.toString())) {
                        result.add(message);
                    }
                }
                return result;
            }
            else {
                throw new IllegalArgumentException("input quantity < 1!");
            }
        }
        else {
            throw new NullPointerException("input message == null!");
        }
    }
}