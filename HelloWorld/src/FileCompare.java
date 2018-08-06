import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class FileCompare {

	protected Shell shlFilecompare;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileCompare window = new FileCompare();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlFilecompare.open();
		shlFilecompare.layout();
		while (!shlFilecompare.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFilecompare = new Shell();
		shlFilecompare.setSize(450, 300);
		shlFilecompare.setText("FileCompare");
		
		Label lblSelectBaseFile = new Label(shlFilecompare, SWT.NONE);
		lblSelectBaseFile.setBounds(32, 35, 107, 15);
		lblSelectBaseFile.setText("Select Base File");
		
		Label lblSelectNewFile = new Label(shlFilecompare, SWT.NONE);
		lblSelectNewFile.setText("Select New File");
		lblSelectNewFile.setBounds(32, 71, 107, 15);

	}
}
