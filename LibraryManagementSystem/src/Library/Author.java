package Library;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

public class Author extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	Connection con;
	PreparedStatement pat;
	ResultSet rs;
	private JTable table;
	
	
	
	public void connect() {
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS","root","1234");
		    
		}
		catch(SQLException e) {
			Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,e);
		}
		catch(ClassNotFoundException e) {
			Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
	public void authorLoad() {
		int c;
		try {
			pat=con.prepareStatement("select * from author");
			rs=pat.executeQuery();

			
			ResultSetMetaData rsd=rs.getMetaData();
			c=rsd.getColumnCount();
			DefaultTableModel d=(DefaultTableModel)table.getModel();
			
			d.setRowCount(0);
			
			while(rs.next()) {
				Vector<String> v= new Vector<String>();
				for(int i=1;i<=c;i++) {
					v.add(rs.getString("id"));
					v.add(rs.getString("name"));
					v.add(rs.getString("address"));
					v.add(rs.getString("phone_no"));
				}
				d.addRow(v);
			}
		}
		catch(SQLException e) {
			Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,e);
		}
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Author frame = new Author();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Author() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 676, 464);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 102, 204));
		contentPane.add(panel, BorderLayout.CENTER);
		
		JTextArea txtAddress = new JTextArea();
		
		
		JLabel lblAuthor = new JLabel("Author");
		lblAuthor.setForeground(Color.WHITE);
		lblAuthor.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblAuthorName = new JLabel("Author Name");
		lblAuthorName.setForeground(new Color(255, 255, 204));
		lblAuthorName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JTextPane txtName = new JTextPane();
		

		JTextPane txtPhone = new JTextPane();
		Author a = this;
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//DefaultTableModel d=(DefaultTableModel)table.getModel();

				String name=txtName.getText();
				String add=txtAddress.getText();
				String phone=txtPhone.getText();
				
				
				try {
					pat=con.prepareStatement("insert into author(name,address,phone_no)values(?,?,?)");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,name);
					pat.setString(2,add);
					pat.setString(3,phone);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Inserted Successfully!!");
						txtName.setText("");
						txtAddress.setText("");
						txtPhone.setText("");
						txtName.requestFocus();
						authorLoad();
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException e)
				{
					Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,e);
			
				}
				
			}
		});
		
		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				//btnNewButton.setEnabled(false);
				int sI=table.getSelectedRow();
				
				int id=Integer.parseInt(d.getValueAt(sI, 0).toString());
				
				
				String name=txtName.getText();
				String add=txtAddress.getText();
				String phone=txtPhone.getText();
				
				try {
					pat=con.prepareStatement("update author set name=?, address=?, phone_no=? where id=?");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,name);
					pat.setString(2,add);
					pat.setString(3,phone);
					pat.setInt(4, id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Updated Successfully!!");
						txtName.setText("");
						txtAddress.setText("");
						txtPhone.setText("");
						txtName.requestFocus();
						authorLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,ex);
			
				}
				
			}
		});
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				int sI=table.getSelectedRow();
				
				int id=Integer.parseInt(d.getValueAt(sI, 0).toString());
				
				
				
				try {
					pat=con.prepareStatement("delete from author where id=?");
					pat.setInt(1,id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row deleted");
						txtName.setText("");
						txtAddress.setText("");
						txtPhone.setText("");
						txtName.requestFocus();
						authorLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Author.class.getName()).log(Level.SEVERE,null,ex);
			
				}
				
		
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.setVisible(false);
			}
		});
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setForeground(new Color(255, 255, 204));
		lblAddress.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		
		JLabel lblNewLabel = new JLabel("PhoneNo.");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(255, 255, 204));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(72)
							.addComponent(lblAuthor))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(85)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnNewButton)
										.addComponent(btnDelete))
									.addGap(45)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(btnCancel)
										.addComponent(btnNewButton_1)))
								.addGroup(gl_panel.createSequentialGroup()
									.addContainerGap()
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
										.addComponent(lblAuthorName)
										.addComponent(lblAddress)
										.addComponent(lblNewLabel))
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(txtName, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
										.addComponent(txtAddress)
										.addComponent(txtPhone))))
							.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addGap(25)
							.addComponent(lblAuthor)
							.addGap(39)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAuthorName)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAddress)
								.addComponent(txtAddress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addComponent(txtPhone, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(69)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNewButton)
								.addComponent(btnNewButton_1))
							.addGap(33)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDelete)
								.addComponent(btnCancel))))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				int si= table.getSelectedRow();
				int id=Integer.parseInt(d.getValueAt(si, 0).toString());
				txtName.setText(d.getValueAt(si,1).toString());
				txtAddress.setText(d.getValueAt(si, 2).toString());
				txtPhone.setText(d.getValueAt(si, 3).toString());
				btnNewButton.setEnabled(false);
				
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Author_Id", "Name", "Address", "Phone_No"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		//add(new JScrollPane(table));
        connect();
        authorLoad();
        
	}
}
