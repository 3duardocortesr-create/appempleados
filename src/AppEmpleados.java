import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AppEmpleados extends JFrame {
    // La URL ahora apunta a un archivo local .db
    static final String URL = "jdbc:sqlite:EmpresaDB.db";

    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtPuesto, txtSalario, txtID;
    private Connection con;

    public AppEmpleados() {
        setTitle("Gestión de Empleados - SQLite");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        conectar();
        crearTablaSiNoExiste(); // Asegura que la base de datos esté lista
        crearInterfaz();
        cargarDatos();
    }

    private void conectar() {
        try {
            // SQLite no requiere usuario ni contraseña
            con = DriverManager.getConnection(URL);
            System.out.println("✔ Conectado a SQLite");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + e.getMessage());
            System.exit(1);
        }
    }

    private void crearTablaSiNoExiste() {
        String sql = "CREATE TABLE IF NOT EXISTS Empleados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "puesto TEXT, " +
                "salario REAL)";
        try (Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error al crear tabla: " + e.getMessage());
        }
    }
    private void crearInterfaz() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));

        panelForm.add(new JLabel("ID:"));
        txtID = new JTextField();
        txtID.setEditable(false);
        panelForm.add(txtID);

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Puesto:"));
        txtPuesto = new JTextField();
        panelForm.add(txtPuesto);

        panelForm.add(new JLabel("Salario:"));
        txtSalario = new JTextField();
        panelForm.add(txtSalario);

        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Puesto", "Salario"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        add(panelForm, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarEmpleado());
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila >= 0) {
                    txtID.setText(modelo.getValueAt(fila, 0).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                    txtPuesto.setText(modelo.getValueAt(fila, 2).toString());
                    txtSalario.setText(modelo.getValueAt(fila, 3).toString());
                }
            }
        });
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        String sql = "SELECT * FROM Empleados";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                modelo.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("puesto"),
                        rs.getDouble("salario")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos: " + e.getMessage());
        }
    }

    private void agregarEmpleado() {
        String nombre = txtNombre.getText();
        String puesto = txtPuesto.getText();
        String salario = txtSalario.getText();

        if (nombre.isEmpty() || puesto.isEmpty() || salario.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa todos los campos");
            return;
        }

        String sql = "INSERT INTO Empleados (nombre, puesto, salario) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, puesto);
            ps.setDouble(3, Double.parseDouble(salario));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✔ Empleado agregado");
            limpiarCampos();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al insertar: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El salario debe ser un número");
        }
    }

    private void actualizarEmpleado() {
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado");
            return;
        }

        String sql = "UPDATE Empleados SET nombre=?, puesto=?, salario=? WHERE id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtPuesto.getText());
            ps.setDouble(3, Double.parseDouble(txtSalario.getText()));
            ps.setInt(4, Integer.parseInt(txtID.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✔ Empleado actualizado");
            limpiarCampos();
            cargarDatos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + e.getMessage());
        }
    }

    private void eliminarEmpleado() {
        if (txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecciona un empleado");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar empleado?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM Empleados WHERE id=?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, Integer.parseInt(txtID.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "🗑 Empleado eliminado");
                limpiarCampos();
                cargarDatos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void limpiarCampos() {
        txtID.setText("");
        txtNombre.setText("");
        txtPuesto.setText("");
        txtSalario.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppEmpleados().setVisible(true));
    }
}