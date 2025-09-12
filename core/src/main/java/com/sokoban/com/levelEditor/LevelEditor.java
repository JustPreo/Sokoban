package com.sokoban.com.levelEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.io.*;

public class LevelEditor extends JFrame {

    private int filas = 10;
    private int columnas = 10;
    private int[][] mapa = new int[filas][columnas];

    private TilePanel tilePanel;
    private int selectedTile = 0; // 0 = piso, 1 = pared, 2 = objetivo, 3 = caja

    //Imagenes - se puede agregar mas a futuro
    private BufferedImage pisoImg;
    private BufferedImage paredImg;
    private BufferedImage objetivoImg;
    private BufferedImage cajaImg;
    private BufferedImage jugadorImg;
    private BufferedImage blankImg;

    private JTextField nombreClaseField;
    private JSpinner filasSpinner;
    private JSpinner columnasSpinner;

    public LevelEditor(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        mapa = new int[filas][columnas];

        loadImages();

        setTitle("Mario Maker 2.0");
        setSize(columnas * 50 + 250, filas * 50 + 50);//Para definir tamano de ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tilePanel = new TilePanel();
        add(new JScrollPane(tilePanel), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(16, 1, 5, 5));

        //Botones basicos
        JButton pisoBtn = new JButton("Piso");
        pisoBtn.addActionListener(e -> selectedTile = 0);
        JButton paredBtn = new JButton("Pared");
        paredBtn.addActionListener(e -> selectedTile = 1);
        JButton objetivoBtn = new JButton("Objetivo");
        objetivoBtn.addActionListener(e -> selectedTile = 2);
        JButton cajaBtn = new JButton("Caja");
        cajaBtn.addActionListener(e -> selectedTile = 3);

        JButton limpiarBtn = new JButton("Limpiar");
        limpiarBtn.addActionListener(e -> {
            mapa = new int[this.filas][this.columnas];
            tilePanel.repaint();
        });
        JButton jugadorBtn = new JButton("Jugador");
        jugadorBtn.addActionListener(e -> selectedTile = 4);
        controlPanel.add(jugadorBtn);

        JButton blankBtn = new JButton("Blank");
        blankBtn.addActionListener(e -> selectedTile = 9);
        controlPanel.add(blankBtn);

        //Nombres - por default esta el nivel 1 pero para mantener orden es eso
        nombreClaseField = new JTextField("MapaNivel1", 10);

        filasSpinner = new JSpinner(new SpinnerNumberModel(this.filas, 1, 50, 1));
        columnasSpinner = new JSpinner(new SpinnerNumberModel(this.columnas, 1, 50, 1));
        //Suma/resta las casillas disponibiles para que el cosito funcione
        JButton aplicarTamanoBtn = new JButton("Aplicar tamano");
        aplicarTamanoBtn.addActionListener(e -> {
            this.filas = (Integer) filasSpinner.getValue();
            this.columnas = (Integer) columnasSpinner.getValue();
            mapa = new int[this.filas][this.columnas];
            tilePanel.setPreferredSize(new Dimension(this.columnas * tilePanel.tileSize, this.filas * tilePanel.tileSize));
            tilePanel.revalidate();
            tilePanel.repaint();
        });

        JButton exportBtn = new JButton("Exportar a clase");
        exportBtn.addActionListener(e -> {
            String nombreClase = nombreClaseField.getText().trim();
            if (!nombreClase.isEmpty()) {
                exportMapaAClase(nombreClase);//Para crear class
                exportMapaATxt(nombreClase);//Para leer
            } else {
                JOptionPane.showMessageDialog(this, "error");
            }
        });

        JButton importarBtn = new JButton("Importar TXT");
        importarBtn.addActionListener(e -> importarMapaDesdeTxt());

        controlPanel.add(pisoBtn);
        controlPanel.add(paredBtn);
        controlPanel.add(objetivoBtn);
        controlPanel.add(cajaBtn);
        controlPanel.add(limpiarBtn);
        controlPanel.add(new JLabel("Nombre de la clase:"));
        controlPanel.add(nombreClaseField);
        controlPanel.add(new JLabel("Filas:"));
        controlPanel.add(filasSpinner);
        controlPanel.add(new JLabel("Columnas:"));
        controlPanel.add(columnasSpinner);
        controlPanel.add(aplicarTamanoBtn);
        controlPanel.add(exportBtn);
        controlPanel.add(importarBtn);

        add(controlPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private void loadImages() {
        try {
            pisoImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/piso1.png"));
            paredImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/pared.png"));
            objetivoImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/objetivo.png"));
            cajaImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/caja.png"));
            jugadorImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/jugador.png"));
            blankImg = ImageIO.read(getClass().getClassLoader().getResourceAsStream("leveleditor/blank.png"));
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error imagenes?");
        }
    }

    private void exportMapaAClase(String nombreClase) {
        try {
            String userDesktop = System.getProperty("user.home")
                    + "/Desktop/SokobanNiveles";
            File folder = new File(userDesktop);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, nombreClase + ".java");
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("package com.sokoban.com.levelEditor;");
                writer.println();
                writer.println("public class " + nombreClase + " {");
                writer.println("    public static final int filas = " + filas + ";");
                writer.println("    public static final int columnas = " + columnas + ";");
                writer.println();

                // Exportar MAPA
                writer.println("    public static final int[][] MAPA = {");
                for (int i = 0; i < filas; i++) {
                    writer.print("        {");
                    for (int j = 0; j < columnas; j++) {
                        writer.print(mapa[i][j]);
                        if (j < columnas - 1) {
                            writer.print(", ");
                        }
                    }
                    writer.println("},");
                }
                writer.println("    };");
                writer.println();

                // Recolectar posiciones (convertimos a x,y donde x=j, y=i)
                StringBuilder cajasSb = new StringBuilder();
                StringBuilder objSb = new StringBuilder();
                int contadorCajas = 0;

                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        if (mapa[i][j] == 3) { // caja
                            if (contadorCajas > 0) {
                                cajasSb.append(", ");
                            }
                            cajasSb.append("{" + j + ", " + i + "}");
                            contadorCajas++;
                        } else if (mapa[i][j] == 2) { // objetivo
                            if (objSb.length() > 0) {
                                objSb.append(", ");
                            }
                            objSb.append("{" + j + ", " + i + "}");
                        }
                    }
                }

                // jugador (buscamos primer tile == 4)
                String jugadorPos = "    public static final int[] JUGADOR_POS = {-1, -1};";
                outer:
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        if (mapa[i][j] == 4) {
                            jugadorPos = "    public static final int[] JUGADOR_POS = {" + j + ", " + i + "};";
                            break outer;
                        }
                    }
                }

                // Escribir arrays (si no hay elementos, se dejan vacios {})
                writer.println("    public static final int[][] CAJAS_POS = {" + (cajasSb.length() == 0 ? "" : " " + cajasSb.toString() + " ") + "};");
                writer.println("    public static final int[][] OBJETIVOS_POS = {" + (objSb.length() == 0 ? "" : " " + objSb.toString() + " ") + "};");
                writer.println(jugadorPos);
                writer.println("    public static final int CANTIDAD_CAJAS = " + contadorCajas + ";");
                writer.println();
                writer.println("}");
            }

            JOptionPane.showMessageDialog(this, "Clase generada en: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generando clase: " + e.getMessage());
        }
    }

    private void exportMapaATxt(String nombreArchivo) {
        try {
            String userDesktop = System.getProperty("user.home") + "/Desktop/SokobanNiveles";
            //Nadiesda , esto lo crea en tu desktop porque me daba error intentar guardarlo por le gradle

            File folder = new File(userDesktop);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File file = new File(folder, nombreArchivo + ".txt");
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println(filas + "," + columnas);
                for (int i = 0; i < filas; i++) {
                    for (int j = 0; j < columnas; j++) {
                        writer.print(mapa[i][j]);
                        if (j < columnas - 1) {
                            writer.print(",");
                        }
                    }
                    writer.println();
                }
            }

            System.out.println("Mapa exportado TXT: " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void importarMapaDesdeTxt() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String firstLine = reader.readLine();
                String[] dims = firstLine.split(",");
                this.filas = Integer.parseInt(dims[0]);
                this.columnas = Integer.parseInt(dims[1]);
                mapa = new int[filas][columnas];

                for (int i = 0; i < filas; i++) {
                    String[] valores = reader.readLine().split(",");
                    for (int j = 0; j < columnas; j++) {
                        mapa[i][j] = Integer.parseInt(valores[j]);
                    }
                }

                filasSpinner.setValue(filas);
                columnasSpinner.setValue(columnas);
                tilePanel.setPreferredSize(new Dimension(columnas * tilePanel.tileSize, filas * tilePanel.tileSize));
                tilePanel.revalidate();
                tilePanel.repaint();

                JOptionPane.showMessageDialog(this, "Mapa importado correctamente.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error mapa: " + e.getMessage());
            }
        }
    }

    class TilePanel extends JPanel {

        private int tileSize = 50;

        public TilePanel() {
            setPreferredSize(new Dimension(columnas * tileSize, filas * tileSize));//size

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    pintarTile(e);
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    pintarTile(e);
                }
            });
        }

        private void pintarTile(MouseEvent e) {
            int x = e.getX() / tileSize;
            int y = e.getY() / tileSize;

            if (x >= 0 && x < LevelEditor.this.columnas && y >= 0 && y < LevelEditor.this.filas) {
                mapa[y][x] = selectedTile;
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < LevelEditor.this.filas; i++) {
                for (int j = 0; j < LevelEditor.this.columnas; j++) {
                    BufferedImage img = pisoImg;//piso por defualt para evitar hacerlo todo manual despues XD
                    switch (mapa[i][j]) {
                        case 0 ->
                            img = pisoImg;//piso
                        case 1 ->
                            img = paredImg;//pared
                        case 2 ->
                            img = objetivoImg;//objective
                        case 3 ->
                            img = cajaImg;//caja
                        case 4 ->
                            img = jugadorImg;//el del jugador
                        case 9 ->
                            img = blankImg;//Sin imagen

                    }
                    g.drawImage(img, j * tileSize, i * tileSize, tileSize, tileSize, null);
                    g.setColor(Color.BLACK);
                    g.drawRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LevelEditor(10, 10));
    }
}
