package task3_rmi;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateTime implements IDateTime {

    private static final DateFormat m_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final DateFormat m_timeFormat = new SimpleDateFormat("HH:mm:ss.sss");

    private static boolean m_stopFlag = false;

    @Override
    public String getDate() throws RemoteException {
        Date date = new Date(System.currentTimeMillis());
        String result = m_dateFormat.format(date);
        System.out.println("> была запрошена дата сервера: " + result);
        return result;
    }

    @Override
    public String getTime() throws RemoteException {
        Date date = new Date(System.currentTimeMillis());
        String result = m_timeFormat.format(date);
        System.out.println("> было запрошено время сервера: " + result);
        return result;
    }

    @Override
    public boolean stop() throws RemoteException {
        m_stopFlag = true;
        return true;
    }

    public boolean getStopFlag() {
        return m_stopFlag;
    }
}