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
        setSize(395, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        getContentPane().setBackground(new Color(0,0,0));

        // Panel resultado
        resultado.setBounds(10, 15, 360, 20);
        resultado.setEditable(false);
        resultado.setHorizontalAlignment(JTextField.RIGHT);
        resultado.setFont(new Font("Arial", Font.PLAIN, 15));
        resultado.setBackground(new Color(0,0,0));
        resultado.setForeground(Color.GRAY);
        resultado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(resultado);

        // Panel pantalla
        pantalla.setBounds(10, 40, 360, 80);
        pantalla.setEditable(false);
        pantalla.setHorizontalAlignment(JTextField.RIGHT);
        pantalla.setFont(new Font("Arial", Font.BOLD, 30));
        pantalla.setBackground(new Color(0,0,0));
        pantalla.setForeground(Color.WHITE);
        pantalla.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        add(pantalla);

        // Boton C (Clear)
        JPanel panelBotonC = new JPanel();
        panelBotonC.setLayout(new GridLayout(1, 1));
        panelBotonC.setBounds(10, 130, 360, 50);
        JButton botonC = new JButton("C");
        botonC.setBackground(Color.GRAY);
        botonC.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        botonC.setForeground(Color.WHITE);

        panelBotonC.add(botonC);
        add(panelBotonC);

        botonC.addActionListener(e -> {
            pantalla.setText("");
            resultado.setText("");
        });

        // Panel botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 4, 5, 5));
        panelBotones.setBounds(10, 190, 360, 340);
        panelBotones.setBackground(new Color(0,0,0));

        // Botones de la calculadora (excepto el igual y la C)
        String[] botones = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                " ", "0", " ", "+"
        };

        // Recorremos el array para crear los botones con menos código
        for (String texto : botones) {
            if (!texto.equals("")) { // No crear botón para el punto
                JButton boton = new JButton(texto);
                panelBotones.add(boton);
                boton.setFont(new Font("Arial", Font.BOLD, 20));
                boton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                if ("+-*/".contains(texto)) {
                    boton.setBackground(new Color(255, 146, 0));
                    boton.setForeground(Color.BLACK);
                } else {
                    boton.setBackground(new Color(47, 47, 47));
                    boton.setForeground(Color.WHITE);
                }
            } else {
                panelBotones.add(new JLabel()); // Espacio vacío
            }
        }

        add(panelBotones);

        // Botón de igual
        JPanel panelBotonIgual = new JPanel();
        panelBotonIgual.setLayout(new GridLayout(1, 1));
        panelBotonIgual.setBounds(10, 540, 360, 50);

        JButton botonIgual = new JButton("=");
        botonIgual.setBackground(new Color(255, 146, 0));
        botonIgual.setForeground(Color.WHITE);
        botonIgual.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelBotonIgual.add(botonIgual);
        add(panelBotonIgual);

        // Calcular resultado
        ActionListener calcularIgual = e -> {
            // Evitar múltiples iguales seguidos
            if (resultado.getText().contains("=")) {
                resultado.setText(resultado.getText());
                return;
            }

            // Evitar cálculo si la pantalla está vacía
            if (pantalla.getText().isEmpty()) {
                return;
            }

            // Mover el número actual a resultado si no se ha hecho ya
            String textoAnterior = resultado.getText();
            if (!textoAnterior.isEmpty()) {
                pantalla.setText(textoAnterior + pantalla.getText());
            }

            // Realizar el cálculo
            String expresion = pantalla.getText();
            resultado.setText(expresion + " =");

            // Calcular el resultado
            String result = calcularResultado(expresion);
            pantalla.setText(result);

            // Marcar que se ha calculado
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
                        case " ":
                            // No hacer nada para espacios
                            break;
                        case "+", "-", "*", "/":
                            // Si se ha calculado, resetear estado
                            if (calculated == true) {
                                calculated = false;
                            }

                            // Evitar operadores al inicio
                            if (resultado.getText().isEmpty() && pantalla.getText().isEmpty()) {
                                break;
                            }

                            // Si ya hay una expresión en resultado y hay un número en pantalla, calcular antes de añadir el nuevo operador
                            if (!resultado.getText().isEmpty() && !pantalla.getText().isEmpty()) {
                                String expresion = resultado.getText().replaceAll("=", "").trim() + pantalla.getText();
                                String result = calcularResultado(expresion);
                                resultado.setText(result + " " + textoBoton + " ");
                                pantalla.setText("");
                                break;
                            }

                            // Reemplazar operador si ya hay uno y la pantalla está vacía
                            if ((resultado.getText().contains("+") || resultado.getText().contains("-")
                                    || resultado.getText().contains("*") || resultado.getText().contains("/"))
                                    && pantalla.getText().isEmpty()) {
                                resultado.setText(resultado.getText().replaceAll("[+\\-*/]", textoBoton));
                                return;
                            }

                            // Solo mover el número actual a resultado si hay algo en pantalla
                            String currentText = pantalla.getText();
                            if (!currentText.isEmpty()) {
                                resultado.setText(currentText + " " + textoBoton + " ");
                                pantalla.setText("");
                            }
                            break;
                        default:
                            // Números
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
                            // Evitar múltiples ceros al inicio
                            if (textoBoton.equals("0") && pantalla.getText().equals("0")) {
                                break;
                            }
                            pantalla.setText(pantalla.getText() + textoBoton);
                            break;
                    }
                });
            }
        }
    }

    // Método para calcular el resultado de la expresión (solo enteros)
    String calcularResultado(String expresion) {
        try {
            for (int i = 0; i < expresion.length(); i++) {
                char c = expresion.charAt(i);
                if ("+-*/".indexOf(c) != -1) {
                    String operando1 = expresion.substring(0, i).trim();
                    String operando2 = expresion.substring(i + 1).trim();
                    if (operando1.isEmpty() || operando2.isEmpty()) {
                        return "Error: Expresión inválida";
                    }
                    int num1 = Integer.parseInt(operando1);
                    int num2 = Integer.parseInt(operando2);
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
