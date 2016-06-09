package ua.artcode.taxi.view.passenger_view;


import ua.artcode.taxi.model.ClientAccessToken;
import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.model.User;
import ua.artcode.taxi.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PassengerHistory extends JFrame {

    private final UserService userService;

    private JLabel userLabel;
    private JTextField userText;
    private JLabel orderLabel;
    private JTextField orderText1;
    private JTextField orderText2;
    private JTextField orderText3;
    private JTextField orderText4;
    private JTextField orderText5;
    private JTextField orderText6;
    private JTextField orderText7;
    private JTextField orderText8;
    private JTextField orderText9;
    private JTextField orderText10;

    private JPanel buttonPanel1;
    private JButton returnButton;

    public PassengerHistory(UserService userService) {

        this.userService = userService;

        setTitle("Main");
        setSize(800, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(14, 1);
        setLayout(gridLayout);

        userLabel = new JLabel("PASSENGER:");
        final User currentUser = userService.getUser(ClientAccessToken.getAccessToken());
        userText = new JTextField("id " + currentUser.getId() +
                                    ", name " + currentUser.getName() +
                                        ", phone " + currentUser.getPhone());
        userText.setEditable(false);

        //create list orders
        String[] textOrders = new String[10];

        List<Order> allUserOrders = userService.getAllOrdersUser(ClientAccessToken.getAccessToken());
        int length = allUserOrders.size() < 10 ? allUserOrders.size() : 10;

        orderLabel = new JLabel("YOU HAVE " + length + " ORDERS:");

        for (int i = length - 1; i >= 0; i--) {
            textOrders[length - 1 - i] = allUserOrders.get(i).toStringForView();
        }
        for (int i = length; i < 10; i++) {
            textOrders[i] = "";
        }

        orderText1 = new JTextField(textOrders[0]);
        orderText1.setEditable(false);
        orderText2 = new JTextField(textOrders[1]);
        orderText2.setEditable(false);
        orderText3 = new JTextField(textOrders[2]);
        orderText3.setEditable(false);
        orderText4 = new JTextField(textOrders[3]);
        orderText4.setEditable(false);
        orderText5 = new JTextField(textOrders[4]);
        orderText5.setEditable(false);
        orderText6 = new JTextField(textOrders[5]);
        orderText6.setEditable(false);
        orderText7 = new JTextField(textOrders[6]);
        orderText7.setEditable(false);
        orderText8 = new JTextField(textOrders[7]);
        orderText8.setEditable(false);
        orderText9 = new JTextField(textOrders[8]);
        orderText9.setEditable(false);
        orderText10 = new JTextField(textOrders[9]);
        orderText10.setEditable(false);

        buttonPanel1 = new JPanel(new GridLayout(1,1));
        returnButton = new JButton("Return to menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PassengerMenu(userService);
            }
        });
        buttonPanel1.add(returnButton);

        getContentPane().add(userLabel);
        getContentPane().add(userText);

        getContentPane().add(orderLabel);

        getContentPane().add(orderText1);
        getContentPane().add(orderText2);
        getContentPane().add(orderText3);
        getContentPane().add(orderText4);
        getContentPane().add(orderText5);
        getContentPane().add(orderText6);
        getContentPane().add(orderText7);
        getContentPane().add(orderText8);
        getContentPane().add(orderText9);
        getContentPane().add(orderText10);

        getContentPane().add(buttonPanel1);
    }
}


