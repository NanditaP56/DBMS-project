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
import javax.swing.JComboBox;

public class Book extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	Connection con;
	PreparedStatement pat;
	ResultSet rs;
	private JTable table;

	JComboBox txtAuthor = new JComboBox();

	JComboBox txtPub = new JComboBox();
	JTextPane txtEd = new JTextPane();
	
	
	public class AuthorItem{
		int id;
		String name;
		
		public AuthorItem(int id, String name) {
			this.id=id;
			this.name=name;
		}
		
		public String toString() {
			return name;
		}
	}
	public class PublisherItem{
		int id;
		String name;
		
		public PublisherItem(int id, String name) {
			this.id=id;
			this.name=name;
		}
		
		public String toString() {
			return name;
		}
	}
	public void connect() {
		
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			con= DriverManager.getConnection("jdbc:mysql://localhost:3306/LMS","root","1234");
		    
		}
		catch(SQLException e) {
			Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,e);
		}
		catch(ClassNotFoundException e) {
			Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,e);
		}
		
	}
	
	public void author() {
		try {
			pat= con.prepareStatement("select * from author");
			rs=pat.executeQuery();
			txtAuthor.removeAllItems();
			
			while(rs.next()) {
				txtAuthor.addItem(new AuthorItem(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	

	public void publisher() {
		try {
			pat= con.prepareStatement("select * from publisher");
			rs=pat.executeQuery();
			txtPub.removeAllItems();
			
			while(rs.next()) {
				txtPub.addItem(new PublisherItem(rs.getInt(1),rs.getString(2)));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	public void bookLoad() {
		int c;
		try {
			pat=con.prepareStatement("select b.Book_id,b.name,a.name,p.name,b.edition from book b join author a on b.author=a.id join publisher p on b.publisher=p.id");
			rs=pat.executeQuery();

			
			ResultSetMetaData rsd=rs.getMetaData();
			c=rsd.getColumnCount();
			DefaultTableModel d=(DefaultTableModel)table.getModel();
			
			d.setRowCount(0);
			
			while(rs.next()) {
				Vector<String> v= new Vector<String>();
				for(int i=1;i<=c;i++) {
					v.add(rs.getString("b.Book_id"));
					v.add(rs.getString("b.name"));
					v.add(rs.getString("a.name"));
					v.add(rs.getString("p.name"));
					v.add(rs.getString("b.edition"));
				}
				d.addRow(v);
			}
		}
		catch(SQLException e) {
			Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,e);
		}
	}

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Book frame = new Book();
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
	public Book() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 784, 492);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(204, 102, 204));
		contentPane.add(panel, BorderLayout.SOUTH);
		
		
		JLabel lblAuthor = new JLabel("Book");
		lblAuthor.setForeground(Color.WHITE);
		lblAuthor.setFont(new Font("Times New Roman", Font.BOLD, 18));
		
		JLabel lblAuthorName = new JLabel(" Name");
		lblAuthorName.setForeground(new Color(255, 255, 204));
		lblAuthorName.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JTextPane txtName = new JTextPane();
		Book a = this;
		
		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//DefaultTableModel d=(DefaultTableModel)table.getModel();

				String name=txtName.getText();
				AuthorItem ai=(AuthorItem)txtAuthor.getSelectedItem();
				PublisherItem pi=(PublisherItem)txtPub.getSelectedItem();
				String ed=txtEd.getText();
				
				
				try {
					pat=con.prepareStatement("insert into book(name,author,publisher,edition)values(?,?,?,?)");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,name);
					pat.setInt(2,ai.id);
					pat.setInt(3,pi.id);
					pat.setString(4,ed);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Inserted Successfully!!");
						txtName.setText("");
						txtAuthor.setSelectedIndex(-1);
						txtPub.setSelectedIndex(-1);
						txtEd.setText("");
						txtName.requestFocus();
						
						bookLoad();
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException e)
				{
					Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,e);
			
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
				AuthorItem ai = (AuthorItem)txtAuthor.getSelectedItem();
				PublisherItem pi=(PublisherItem)txtPub.getSelectedItem();
				String ed=txtEd.getText();
				
				try {
					pat=con.prepareStatement("update book set name=?, author=?, publisher=?,edition=? where Book_id=?");
					//pat.setString(1,name.substring(0,5)+"001");
					pat.setString(1,name);
					pat.setInt(2,ai.id);
					pat.setInt(3,pi.id);
					pat.setString(4,ed);
					pat.setInt(5, id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row Updated Successfully!!");
						txtName.setText("");
						txtAuthor.setSelectedIndex(-1);
						txtPub.setSelectedIndex(-1);
						txtEd.setText("");
						txtName.requestFocus();
						bookLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,ex);
			
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
					pat=con.prepareStatement("delete from book where Book_id=?");
					pat.setInt(1,id);
					
					int k= pat.executeUpdate();
					
					if(k==1) {
						JOptionPane.showMessageDialog(a, "Row deleted !!");
						txtName.setText("");
						txtPub.setSelectedIndex(-1);;
						txtAuthor.setSelectedIndex(-1);
						txtEd.setText("");
						txtName.requestFocus();
						bookLoad();
						btnNewButton.setEnabled(true);
					}
					else {
						JOptionPane.showMessageDialog(a,"Error!!");
					}
					
				}
				catch(SQLException ex)
				{
					Logger.getLogger(Book.class.getName()).log(Level.SEVERE,null,ex);
			
				}
				
		
			}
		});
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				a.setVisible(false);
			}
		});
		
		
		JLabel lblNewLabel = new JLabel("Author");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		lblNewLabel.setForeground(new Color(255, 255, 204));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
			}
		});
		
		JLabel lblPublisher = new JLabel("Publisher");
		lblPublisher.setForeground(new Color(255, 255, 204));
		lblPublisher.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JLabel lblEdition = new JLabel("Edition");
		lblEdition.setForeground(new Color(255, 255, 204));
		lblEdition.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		
		
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(72)
							.addComponent(lblAuthor))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblAuthorName)
							.addGap(24)
							.addComponent(txtName, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(85)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnDelete)
								.addComponent(btnNewButton))
							.addGap(43)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNewButton_1)
								.addComponent(btnCancel)))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)
							.addGap(18)
							.addComponent(txtAuthor, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblPublisher)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtPub, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblEdition, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtEd, GroupLayout.PREFERRED_SIZE, 205, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(25)
							.addComponent(lblAuthor)
							.addGap(39)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblAuthorName)
								.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel)
								.addComponent(txtAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPublisher)
								.addComponent(txtPub, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblEdition, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
								.addComponent(txtEd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED, 128, Short.MAX_VALUE)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnNewButton_1)
								.addComponent(btnNewButton))
							.addGap(18)
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnDelete)
								.addComponent(btnCancel))))
					.addGap(38))
		);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				DefaultTableModel d=(DefaultTableModel)table.getModel();
				int si= table.getSelectedRow();
				//int x=d.getValueAt(si, 3)
				//AuthorItem ai=(AuthorItem)txtAuthor.getSelectedItem();
				//PublisherItem pi=(PublisherItem)txtPub.getSelectedItem();
				int id=Integer.parseInt(d.getValueAt(si, 0).toString());
				txtName.setText(d.getValueAt(si,1).toString());
				txtAuthor.setSelectedItem(d.getValueAt(si, 2).toString());
				txtPub.setSelectedItem(d.getValueAt(si, 3).toString());
				txtEd.setText(d.getValueAt(si, 4).toString());
				btnNewButton.setEnabled(false); 
				
			}
		});
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Book_Id", "Book_Name", "Author", "Publisher", "Edition"
			}
		) {
			Class[] columnTypes = new Class[] {
				Integer.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		panel.setLayout(gl_panel);
		//add(new JScrollPane(table));
        connect();
        bookLoad();
        author();
        publisher();
	}
}
