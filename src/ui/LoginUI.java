package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginUI {
  public static void main(String[] args) {
    new LoginUI();
  }

  private JFrame frame;
  private JTextField emailField;
  private JPasswordField passwordField;
  private JButton loginButton;

  public LoginUI() {
    frame = new JFrame("Login Applikasi Perpustakaan");
    frame.setSize(200, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);

    JPanel panel = new JPanel();
    frame.add(panel);
    placeComponents(panel);

  }

  public JFrame getFrame() {
    return frame;
  }

  public void addActionListenerLoginButton(ActionListener listener) {
    loginButton.addActionListener(listener);
  }

  public String getEmail() {
    return emailField.getText();
  }

  public String getPassword() {
    return new String(passwordField.getPassword());
  }

  public void setEmail(String email) {
    emailField.setText(email);
  }

  public void setPassword(String password) {
    passwordField.setText(password);
  }

  private void placeComponents(JPanel panel) {
    panel.setLayout(null);

    JLabel titleLogin = new JLabel("Please Login");
    titleLogin.setBounds(60, 0, 80, 25);
    panel.add(titleLogin);

    JLabel userLabel = new JLabel("Email:");
    userLabel.setBounds(10, 20, 80, 25);
    panel.add(userLabel);

    emailField = new JTextField(20);
    emailField.setBounds(10, 40, 165, 25);
    panel.add(emailField);

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setBounds(10, 70, 80, 25);
    panel.add(passwordLabel);

    passwordField = new JPasswordField(20);
    passwordField.setBounds(10, 90, 165, 25);
    passwordField.setEchoChar('*');
    passwordField.addKeyListener(new KeyAdapter() {
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          loginButton.doClick();
        }
      }
    });

    panel.add(passwordField);

    loginButton = new JButton("login");
    loginButton.setBounds(60, 130, 80, 25);
    panel.add(loginButton);
  }
}