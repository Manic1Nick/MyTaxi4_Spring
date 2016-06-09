package ua.artcode.taxi.view.driver_view;

import ua.artcode.taxi.model.Order;
import ua.artcode.taxi.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Map;

public class CurrentOrders extends JFrame {

    private final UserService userService;

    private JLabel mainLabel;

    private JPanel buttonPanel1;
    private JButton findOrdersButton;
    private JButton findLocationButton;

    private JLabel myLocationLabel;
    private JTextField myLocationText;

    private JLabel currentOrdersLabel;

    private JPanel ordersButtonPanel1;
    private JButton infoOrderButton1;

    private JPanel ordersButtonPanel2;
    private JButton infoOrderButton2;

    private JPanel ordersButtonPanel3;
    private JButton infoOrderButton3;

    private JPanel ordersButtonPanel4;
    private JButton infoOrderButton4;

    private JPanel ordersButtonPanel5;
    private JButton infoOrderButton5;

    private JPanel ordersButtonPanel6;
    private JButton infoOrderButton6;

    private JPanel ordersButtonPanel7;
    private JButton infoOrderButton7;

    private JPanel ordersButtonPanel8;
    private JButton infoOrderButton8;

    private JPanel ordersButtonPanel9;
    private JButton infoOrderButton9;

    private JPanel ordersButtonPanel10;
    private JButton infoOrderButton10;


    private JPanel buttonPanel2;

    private JButton showMapButton;
    private JButton returnButton;

    public CurrentOrders(UserService userService){

        this.userService = userService;

        setTitle("Main");
        setSize(400, 600);
        init();
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void init() {
        GridLayout gridLayout = new GridLayout(16, 2);
        setLayout(gridLayout);

        mainLabel = new JLabel("CURRENT ORDERS");

        myLocationLabel = new JLabel("MY LOCATION:");
        myLocationText = new JTextField();
        myLocationText.setText("Ukraine Kiev Starokievskaya 10");
        myLocationText.setEditable(false);

        currentOrdersLabel = new JLabel("NEW ORDERS CLOSEST TO YOU:");

        //max quantity new orders for driver
        final int viewOrders = 10;
        String[] textOrders = new String[viewOrders];

        Map<Integer, Order> distanceMap = userService.getMapDistancesToDriver("NEW", myLocationText.getText());

        Object[] objArray = distanceMap.keySet().toArray();
        int[] distances = new int[objArray.length];
        for (int i = 0; i < objArray.length; i++) {
            distances[i] = (int) objArray[i];
        }
        Arrays.sort(distances);

        for (int i = 0; i < viewOrders; i++) {
            textOrders[i] = distanceMap.get(distances[i]).toStringForViewShort() +
                    ", distance to you: "  + distances[i]/1000 + "km";
        }

        if (distanceMap.size() < viewOrders) {
            for (int i = distanceMap.size(); i < viewOrders; i++) {
                textOrders[i] = "";
            }
        }

        ordersButtonPanel1 = new JPanel(new GridLayout(1,1));
        infoOrderButton1 = new JButton(textOrders[0]);
        infoOrderButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton1.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[0]));
                }
            }
        });
        ordersButtonPanel1.add(infoOrderButton1);

        ordersButtonPanel2 = new JPanel(new GridLayout(1,1));
        infoOrderButton2 = new JButton(textOrders[1]);
        infoOrderButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton2.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[1]));
                }
            }
        });
        ordersButtonPanel2.add(infoOrderButton2);

        ordersButtonPanel3 = new JPanel(new GridLayout(1,1));
        infoOrderButton3 = new JButton(textOrders[2]);
        infoOrderButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton3.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[2]));
                }
            }
        });
        ordersButtonPanel3.add(infoOrderButton3);

        ordersButtonPanel4 = new JPanel(new GridLayout(1,1));
        infoOrderButton4 = new JButton(textOrders[3]);
        infoOrderButton4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton4.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[3]));
                }
            }
        });
        ordersButtonPanel4.add(infoOrderButton4);

        ordersButtonPanel5 = new JPanel(new GridLayout(1,1));
        infoOrderButton5 = new JButton(textOrders[4]);
        infoOrderButton5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton5.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[4]));
                }
            }
        });
        ordersButtonPanel5.add(infoOrderButton5);

        ordersButtonPanel6 = new JPanel(new GridLayout(1,1));
        infoOrderButton6 = new JButton(textOrders[5]);
        infoOrderButton6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton6.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[5]));
                }
            }
        });
        ordersButtonPanel6.add(infoOrderButton6);

        ordersButtonPanel7 = new JPanel(new GridLayout(1,1));
        infoOrderButton7 = new JButton(textOrders[6]);
        infoOrderButton7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton7.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[6]));
                }
            }
        });
        ordersButtonPanel7.add(infoOrderButton7);

        ordersButtonPanel8 = new JPanel(new GridLayout(1,1));
        infoOrderButton8 = new JButton(textOrders[7]);
        infoOrderButton8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton8.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[7]));
                }
            }
        });
        ordersButtonPanel8.add(infoOrderButton8);

        ordersButtonPanel9 = new JPanel(new GridLayout(1,1));
        infoOrderButton9 = new JButton(textOrders[8]);
        infoOrderButton9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton9.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[8]));
                }
            }
        });
        ordersButtonPanel9.add(infoOrderButton9);

        ordersButtonPanel10 = new JPanel(new GridLayout(1,1));
        infoOrderButton10 = new JButton(textOrders[9]);
        infoOrderButton10.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!infoOrderButton10.getText().equals("")) {
                    dispose();
                    new DriverOrderInfo(userService, distanceMap.get(distances[9]));
                }
            }
        });
        ordersButtonPanel10.add(infoOrderButton10);

        //------------------------------------------------------------------
        //buttons
        buttonPanel1 = new JPanel(new GridLayout(1,1));
        findOrdersButton = new JButton("Update new orders");
        findOrdersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CurrentOrders(userService);
                dispose();
            }
        });
        buttonPanel1.add(findOrdersButton);

        findLocationButton = new JButton("Update my location");
        findLocationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //use Google API ?
                myLocationText.setText("Ukraine Kiev Starokievskaya 10");
            }
        });
        buttonPanel1.add(findLocationButton);

        buttonPanel2 = new JPanel(new GridLayout(1, 1));
        showMapButton = new JButton("Show map");
        showMapButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(getParent(), "You need use Java Fix :)");
            }
        });
        returnButton = new JButton("Return to menu");
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new DriverMenu(userService);
            }
        });
        buttonPanel2.add(showMapButton);
        buttonPanel2.add(returnButton);


        getContentPane().add(mainLabel);

        getContentPane().add(buttonPanel1);
        getContentPane().add(buttonPanel2);

        getContentPane().add(myLocationLabel);
        getContentPane().add(myLocationText);

        getContentPane().add(currentOrdersLabel);

        if (!textOrders[0].equals("")) {
            getContentPane().add(ordersButtonPanel1);
        }
        if (!textOrders[1].equals("")) {
            getContentPane().add(ordersButtonPanel2);
        }
        if (!textOrders[2].equals("")) {
            getContentPane().add(ordersButtonPanel3);
        }
        if (!textOrders[3].equals("")) {
            getContentPane().add(ordersButtonPanel4);
        }
        if (!textOrders[4].equals("")) {
            getContentPane().add(ordersButtonPanel5);
        }
        if (!textOrders[5].equals("")) {
            getContentPane().add(ordersButtonPanel6);
        }
        if (!textOrders[6].equals("")) {
            getContentPane().add(ordersButtonPanel7);
        }
        if (!textOrders[7].equals("")) {
            getContentPane().add(ordersButtonPanel8);
        }
        if (!textOrders[8].equals("")) {
            getContentPane().add(ordersButtonPanel9);
        }
        if (!textOrders[9].equals("")) {
            getContentPane().add(ordersButtonPanel10);
        }
    }
}
