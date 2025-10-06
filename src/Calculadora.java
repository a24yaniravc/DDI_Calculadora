import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Calculadora extends JFrame {
    private JTextField resultado = new JTextField();
    private JTextField pantalla = new JTextField();
    private Boolean calculated = false;

    public Calculadora() {
        // Configuración ventana
        setTitle("Calculadora");
        setSize(395, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Panel resultado
        resultado.setBounds(10, 10, 360, 20);
        resultado.setEditable(false);
        resultado.setHorizontalAlignment(JTextField.RIGHT);
        add(resultado);

        // Panel pantalla
        pantalla.setBounds(10, 40, 360, 80);
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        add(pantalla);

        // Panel botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 4, 5, 5));
        panelBotones.setBounds(10, 130, 360, 340);

        // Botones de la calculadora
        String[] botones = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "C", "0", ".", "+"
        };

        // Recorremos el array para crear los botones con menos código
        for (String texto : botones) {
            JButton boton = new JButton(texto);
            panelBotones.add(boton);
        }

        add(panelBotones);

        // Botón de igual
        JPanel panelBotonIgual = new JPanel();
        panelBotonIgual.setLayout(new GridLayout(1, 1));
        panelBotonIgual.setBounds(10, 480, 360, 50);

        JButton botonIgual = new JButton("=");
        panelBotonIgual.add(botonIgual);
        add(panelBotonIgual);

        // Calcular resultado
        ActionListener calcularIgual = e -> {
            if (resultado.getText().contains("=")) {
                resultado.setText(resultado.getText());
                return;
            }
            if (pantalla.getText().isEmpty()) {
                return;
            }
            String textoAnterior = resultado.getText();
            if (!textoAnterior.isEmpty()) {
                pantalla.setText(textoAnterior + pantalla.getText());
            }
            String expresion = pantalla.getText();
            resultado.setText(expresion + " =");
            String result = calcularResultado(expresion);
            pantalla.setText(result);
            calculated = true;
        };

        botonIgual.addActionListener(calcularIgual);

        // Introducción de funcionalidad botones
        for (Component comp : panelBotones.getComponents()) {
            if (comp instanceof JButton) {
                JButton boton = (JButton) comp;
                boton.addActionListener(e -> {
                    String textoBoton = boton.getText();
                    switch (textoBoton) {
                        case "C":
                            pantalla.setText("");
                            resultado.setText("");
                            break;
                        case "+", "-", "*", "/":
                            if (resultado.getText().isEmpty() && pantalla.getText().isEmpty()) {
                                break;
                            } // No permitir operador al inicio

                            if (resultado.getText().contains("+") || resultado.getText().contains("-")
                                    || resultado.getText().contains("*") || resultado.getText().contains("/")) {
                                resultado.setText(resultado.getText());
                            }

                            if (!resultado.getText().isEmpty() && !pantalla.getText().isEmpty()) {

                            }
                            String currentText = pantalla.getText();
                            pantalla.setText("");
                            resultado.setText(currentText + " " + textoBoton + " ");
                            break;
                        default:
                            if (calculated == true) {
                                pantalla.setText("");
                                resultado.setText("");
                                calculated = false;
                            }
                            if (pantalla.getText().equals("Error: División por cero")
                                    || pantalla.getText().equals("Error: Expresión inválida")) {
                                pantalla.setText("");
                                resultado.setText("");
                            }
                            pantalla.setText(pantalla.getText() + textoBoton);
                            break;
                    }
                });
            }
        }
    }

    String calcularResultado(String expresion) {
        try {
            for (int i = 0; i < expresion.length(); i++) {
                char c = expresion.charAt(i);
                if ("+-*/".indexOf(c) != -1) {
                    String operando1 = expresion.substring(0, i);
                    String operando2 = expresion.substring(i + 1);
                    if (operando1.isEmpty() || operando2.isEmpty()) {
                        return "Error: Expresión inválida";
                    }
                    double num1 = Double.parseDouble(operando1);
                    double num2 = Double.parseDouble(operando2);
                    pantalla.setText("");

                    switch (c) {
                        case '+':
                            return String.valueOf(num1 + num2);
                        case '-':
                            return String.valueOf(num1 - num2);
                        case '*':
                            return String.valueOf(num1 * num2);
                        case '/':
                            if (num2 == 0) {
                                return "Error: División por cero";
                            }
                            return String.valueOf(num1 / num2);
                    }
                }
            }
            // Si no se encuentra operador
            if (expresion.isEmpty()) {
                return "";
            }
            return expresion;
        } catch (NumberFormatException ex) {
            return "Error: Expresión inválida";
        }
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(() -> {
            Calculadora ventana = new Calculadora();
            ventana.setVisible(true);
        });
    }
}
