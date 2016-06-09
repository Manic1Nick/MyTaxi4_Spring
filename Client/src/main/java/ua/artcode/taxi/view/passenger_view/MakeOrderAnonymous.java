package ua.artcode.taxi.view.passenger_view;

import ua.artcode.taxi.exception.InputDataWrongException;
import ua.artcode.taxi.exception.OrderMakeException;
import ua.artcode.taxi.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class MakeOrderAnonymous extends JFrame {

    private final UserService userService;

    private JLabel mainLabel;
    private JLabel nullLabel;

    private JLabel phoneLabel;
    private JTextField phoneText;
    private JLabel nameLabel;
    private JTextField nameText;

    private JLabel fromLabel;
    private JTextField fromText;
    private JLabel toLabel;
    private JTextField toText;
    private JLabel distanceLabel;
    private JTextField distanceText;
    private JLabel priceLabel;
    private JTextField priceText;
    private JLabel messageLabel;
    private JTextField messageText;

    private JPanel buttonPanel1;
    private JPanel buttonPanel2;
    private JPanel buttonPanel3;
    private JPanel buttonPanel4;
    private JPanel buttonPanel5;
    private JPanel buttonPanel6;

    private JButton usePreviousButton;
    private JButton findLocationButton;
    private JButton useHomeAddressButton;
    private JButton returnButton;

    private JButton makeOrderButton;
    private JButton calculateButton;

    public MakeOrderAnonymous(UserService userService){

        this.userService = userService;

        setTitle("Main");
        setSize(400, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(11, 1);
        setLayout(gridLayout);

        mainLabel = new JLabel("MAKE ORDER ANONYMOUS");
        nullLabel = new JLabel("");

        buttonPanel1 = new JPanel(new GridLayout(1, 1));
        usePreviousButton = new JButton("Use previous");
        usePreviousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getParent(), "You must registered for using previous order");
            }
        });
        buttonPanel1.add(usePreviousButton);

        buttonPanel2 = new JPanel(new GridLayout(1, 1));
        findLocationButton = new JButton("Use my location");
        findLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fromText.setText("Ukraine Kiev Starokievskaya 10");
            }
        });
        buttonPanel2.add(findLocationButton);

        buttonPanel3 = new JPanel(new GridLayout(1, 2));
        useHomeAddressButton = new JButton("Use my home address");
        useHomeAddressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getParent(), "You must registered for using home address");
            }
        });
        buttonPanel3.add(useHomeAddressButton);

        buttonPanel4 = new JPanel(new GridLayout(1, 1));
        returnButton = new JButton("Return to menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new PassengerMenu(userService);
            }
        });
        buttonPanel4.add(returnButton);

        phoneLabel = new JLabel("PHONE:");
        phoneText = new JTextField();

        nameLabel = new JLabel("NAME:");
        nameText = new JTextField();

        fromLabel = new JLabel("FROM:");
        fromText = new JTextField();

        toLabel = new JLabel("TO:");
        toText = new JTextField();

        distanceLabel = new JLabel("DISTANCE, km:");
        distanceText = new JTextField();
        distanceText.setEditable(false);

        priceLabel = new JLabel("PRICE, uah:");
        priceText = new JTextField();
        priceText.setEditable(false);

        messageLabel = new JLabel("YOUR MESSAGE:");
        messageText = new JTextField();

        buttonPanel5 = new JPanel(new GridLayout(1, 1));
        makeOrderButton = new JButton("Make Order");
        makeOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //check input data
                String[] from = fromText.getText().split(" ");
                String[] to = toText.getText().split(" ");

                String message = "";

                if (phoneText.getText().equals("")) {
                    message = "Please enter your PHONE";
                } else if (nameText.getText().equals("")) {
                    message = "Please enter your NAME";
                } else if (fromText.getText().equals("")) {
                    message = "Please enter FROM address";
                } else if (toText.getText().equals("")) {
                    message = "Please enter TO address";
                } else if (from.length < 4) {
                    message = "Please enter FROM address in format COUNTRY CITY STREET HOMENUM";
                } else if (to.length < 4) {
                    message = "Please enter TO address in format COUNTRY CITY STREET HOMENUM";
                }

                if (!message.equals("")) {
                    JOptionPane.showMessageDialog(getParent(), message);

                } else if (message.equals("")) {
                    try {
                        userService.makeOrderAnonymous(phoneText.getText(), nameText.getText(),
                                fromText.getText(), toText.getText(), messageText.getText());

                        dispose();
                        new PassengerMenu(userService);
                        JOptionPane.showMessageDialog(getParent(), "Your order was created anonymous");

                    } catch (OrderMakeException e1) {
                        JOptionPane.showMessageDialog(getParent(),"Order can not make");
                        e1.printStackTrace();
                    } catch (InputDataWrongException e1) {
                        JOptionPane.showMessageDialog(getParent(),"Input data wrong");
                        e1.printStackTrace();
                    }
                }

            }
        });
        buttonPanel5.add(makeOrderButton);

        buttonPanel6 = new JPanel(new GridLayout(1, 1));
        calculateButton = new JButton("Calculate Price");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //check input data
                String[] from = fromText.getText().split(" ");
                String[] to = toText.getText().split(" ");

                String message = "";

                if (fromText.getText().equals("")) {
                    message = "Please enter FROM address!";
                } else if (toText.getText().equals("")) {
                    message = "Please enter TO address!";
                } else if (from.length < 4) {
                    message = "Please enter FROM address in format COUNTRY CITY STREET HOMENUM";
                } else if (to.length < 4) {
                    message = "Please enter TO address in format COUNTRY CITY STREET HOMENUM";
                }

                if (!message.equals("")) {
                    JOptionPane.showMessageDialog(getParent(), message);

                } else if (message.equals("")) {
                    try {
                        Map<String, Object> map = userService.calculateOrder(fromText.getText(), toText.getText());

                        distanceText.setText(map.get("distance") + "");
                        priceText.setText(map.get("price") + "");

                    } catch (InputDataWrongException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        buttonPanel6.add(calculateButton);

        getContentPane().add(mainLabel);
        getContentPane().add(nullLabel);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);
        getContentPane().add(buttonPanel3);
        getContentPane().add(buttonPanel4);

        getContentPane().add(phoneLabel);
        getContentPane().add(phoneText);
        getContentPane().add(nameLabel);
        getContentPane().add(nameText);

        getContentPane().add(fromLabel);
        getContentPane().add(fromText);
        getContentPane().add(toLabel);
        getContentPane().add(toText);
        getContentPane().add(distanceLabel);
        getContentPane().add(distanceText);
        getContentPane().add(priceLabel);
        getContentPane().add(priceText);
        getContentPane().add(messageLabel);
        getContentPane().add(messageText);

        getContentPane().add(buttonPanel5);
        getContentPane().add(buttonPanel6);
    }
}
