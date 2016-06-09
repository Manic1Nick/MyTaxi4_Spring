package ua.artcode.taxi.view;

import ua.artcode.taxi.exception.OrderNotFoundException;
import ua.artcode.taxi.exception.UserNotFoundException;
import ua.artcode.taxi.model.ClientAccessToken;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.model.UserIdentifier;
import ua.artcode.taxi.service.UserService;
import ua.artcode.taxi.view.driver_view.DriverOrderInfo;
import ua.artcode.taxi.view.passenger_view.PassengerOrderInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageAdd extends JFrame {

    private final UserService userService;
    private final Order myOrder;

    private JLabel mainLabel;

    private JPanel messagePanel;
    private JTextField messageText;

    private JPanel buttonPanel1;
    private JPanel buttonPanel2;

    private JButton addMessageButton;
    private JButton returnButton;

    public MessageAdd(UserService userService, Order myOrder){

        this.userService = userService;
        this.myOrder = myOrder;

        setTitle("Main");
        setSize(400, 200);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(2, 1);
        setLayout(gridLayout);

        mainLabel = new JLabel("ADD MESSAGE");

        messagePanel = new JPanel(new GridLayout(1,1));
        messageText = new JTextField();

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        addMessageButton = new JButton("Add message");
        addMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User currentUser = userService.getUser(ClientAccessToken.getAccessToken());

                String newMessage = messageText.getText().equals("") ? "" :
                        currentUser.getName() + ": " + messageText.getText() ;
                myOrder.setMessage(newMessage);
                dispose();

                if (currentUser.getIdentifier().equals(UserIdentifier.P)) {
                    new PassengerOrderInfo(userService, myOrder);
                } else if (currentUser.getIdentifier().equals(UserIdentifier.D)) {
                    new DriverOrderInfo(userService, myOrder);
                }
            }
        });

        buttonPanel2 = new JPanel(new GridLayout(1,1));
        returnButton = new JButton("Return");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    User currentUser = userService.getUser(ClientAccessToken.accessToken);

                    Order currentOrder = userService.getOrderInfo(userService.getLastOrderInfo
                            (ClientAccessToken.accessToken).getId());
                    dispose();

                    if (currentUser.getIdentifier().equals(UserIdentifier.P)) {
                        new PassengerOrderInfo(userService, currentOrder);

                    } else if (currentUser.getIdentifier().equals(UserIdentifier.D)) {
                        new DriverOrderInfo(userService, currentOrder);
                    }

                } catch (OrderNotFoundException e1) {
                    e1.printStackTrace();
                } catch (UserNotFoundException e1) {
                    e1.printStackTrace();
                }

            }
        });

        getContentPane().add(mainLabel);

        getContentPane().add(messagePanel);
        messagePanel.add(messageText);

        buttonPanel1.add(addMessageButton);
        buttonPanel2.add(returnButton);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);
    }
}
