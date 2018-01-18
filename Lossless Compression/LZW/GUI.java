import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GUI extends JFrame  {
	
	private static final long serialVersionUID = 1L;
	private static JPanel browse;
	private static JPanel perform;
	
	
	private static JButton compress ;
	private static JButton decompress;
	private static JButton findFile;
	
	private static JTextArea  filepath;

	private	static JFileChooser fileChosser;
	
	private  static File selectedFile;
	
	
	private static JTextField error;
	
	public GUI() {
		super("LZW");
		setLayout(new BorderLayout());
		
		
		browse = new JPanel();
		perform = new JPanel();
		
		compress = new JButton("Compress");
		decompress = new JButton("deCompress");
		findFile   = new JButton("Select File");
		
		error = new JTextField();
		error.setForeground(Color.red);
		filepath = new JTextArea("Selected FILE PATH");
		
		fileChosser = new JFileChooser();

		selectedFile = new File("");
		
		browse.add(findFile,BorderLayout.EAST);
		browse.add(filepath,BorderLayout.WEST);
		

		perform.add(compress,BorderLayout.WEST);	
		perform.add(decompress,BorderLayout.EAST);

		
		add(browse,BorderLayout.NORTH);
		add(perform,BorderLayout.CENTER);
		add(error,BorderLayout.SOUTH);

		
		Handler handle=new Handler();
		compress.addActionListener(handle);
		decompress.addActionListener(handle);
		findFile.addActionListener(handle);

	}
	
	public static void setErrorText(String msg) {
		error.setText(msg);
	}
	
	private class Handler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==findFile){
				int returnValue = fileChosser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
			          selectedFile = fileChosser.getSelectedFile();
			          filepath.setText(selectedFile.getPath());
			          System.out.println(selectedFile.getPath());
				
				}
			}
			else if(e.getSource()==compress){
				System.out.println(selectedFile.getPath());
				try {
					LZW.compress(selectedFile.getPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else if(e.getSource()==decompress){
				try {
					LZW.decompress(selectedFile.getPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}			
			
		}
	}
}
