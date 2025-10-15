import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

public class FacialExpressionRecognizer extends JFrame {
    private JLabel cameraLabel;
    private JLabel imageLabel;
    private VideoCapture camera;
    private CascadeClassifier faceCascade;
    private CascadeClassifier eyeCascade;
    private CascadeClassifier smileCascade;
    private Mat frame;
    private boolean running;
    
    // Image file paths - CHANGE THESE TO YOUR IMAGE PATHS
    private static final String IMAGE_FOLDER = "images/"; // Folder containing your images
    private static final String IMAGE_NEUTRAL = IMAGE_FOLDER + "neutral.png";
    private static final String IMAGE_HAPPY = IMAGE_FOLDER + "happy.png";
    private static final String IMAGE_SURPRISED = IMAGE_FOLDER + "surprised.png";
    private static final String IMAGE_SAD = IMAGE_FOLDER + "sad.png";
    private static final String IMAGE_WINK = IMAGE_FOLDER + "wink.png";
    
    // Expression constants
    private static final String EXPR_NEUTRAL = "NEUTRAL";
    private static final String EXPR_HAPPY = "HAPPY";
    private static final String EXPR_SURPRISED = "SURPRISED";
    private static final String EXPR_SAD = "SAD";
    private static final String EXPR_WINK = "WINK";
    
    private Map<String, ImageIcon> expressionImages;
    private String currentExpression = EXPR_NEUTRAL;
    
    public FacialExpressionRecognizer() {
        setTitle("Facial Expression Recognition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Load expression images
        loadExpressionImages();
        
        // Create panel for camera and image
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        
        // Camera feed on the left
        cameraLabel = new JLabel();
        cameraLabel.setHorizontalAlignment(JLabel.CENTER);
        cameraLabel.setVerticalAlignment(JLabel.CENTER);
        cameraLabel.setBorder(BorderFactory.createTitledBorder("Camera Feed"));
        mainPanel.add(cameraLabel);
        
        // Image display on the right
        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setBorder(BorderFactory.createTitledBorder("Detected Expression"));
        imageLabel.setIcon(expressionImages.get(EXPR_NEUTRAL));
        mainPanel.add(imageLabel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Status label
        JLabel statusLabel = new JLabel("Initializing...", JLabel.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        
        setSize(1200, 600);
        setLocationRelativeTo(null);
        
        // Load OpenCV native library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        // Initialize cascades
        faceCascade = new CascadeClassifier("haarcascade_frontalface_default.xml");
        eyeCascade = new CascadeClassifier("haarcascade_eye.xml");
        smileCascade = new CascadeClassifier("haarcascade_smile.xml");
        
        if (faceCascade.empty() || eyeCascade.empty() || smileCascade.empty()) {
            statusLabel.setText("Error: Could not load cascade classifiers. Please ensure XML files are in the project directory.");
            return;
        }
        
        // Initialize camera
        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            statusLabel.setText("Error: Could not open camera");
            return;
        }
        
        frame = new Mat();
        running = true;
        statusLabel.setText("Running - Press window close to exit | Images loaded: " + expressionImages.size());
        
        // Start capture thread
        new Thread(() -> {
            while (running) {
                captureAndProcess();
                try {
                    Thread.sleep(33); // ~30 FPS
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            camera.release();
        }).start();
    }
    
    private void loadExpressionImages() {
        expressionImages = new HashMap<>();
        
        // Create images folder if it doesn't exist
        File imageFolder = new File(IMAGE_FOLDER);
        if (!imageFolder.exists()) {
            imageFolder.mkdirs();
            System.out.println("Created images folder at: " + imageFolder.getAbsolutePath());
            System.out.println("Please add your expression images there!");
        }
        
        // Try to load each expression image
        expressionImages.put(EXPR_NEUTRAL, loadImage(IMAGE_NEUTRAL, "😐"));
        expressionImages.put(EXPR_HAPPY, loadImage(IMAGE_HAPPY, "😊"));
        expressionImages.put(EXPR_SURPRISED, loadImage(IMAGE_SURPRISED, "😮"));
        expressionImages.put(EXPR_SAD, loadImage(IMAGE_SAD, "😢"));
        expressionImages.put(EXPR_WINK, loadImage(IMAGE_WINK, "😉"));
    }
    
    private ImageIcon loadImage(String path, String fallbackEmoji) {
        File imageFile = new File(path);
        
        if (imageFile.exists()) {
            try {
                BufferedImage img = ImageIO.read(imageFile);
                // Scale image to fit nicely (max 400x400)
                Image scaledImg = img.getScaledInstance(400, 400, Image.SCALE_SMOOTH);
                System.out.println("Loaded image: " + path);
                return new ImageIcon(scaledImg);
            } catch (Exception e) {
                System.out.println("Error loading image " + path + ": " + e.getMessage());
            }
        } else {
            System.out.println("Image not found: " + path + " - using emoji fallback");
        }
        
        // Fallback to emoji if image not found
        return createEmojiIcon(fallbackEmoji);
    }
    
    private ImageIcon createEmojiIcon(String emoji) {
        // Create a label with emoji as fallback
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 200));
        emojiLabel.setSize(400, 400);
        
        BufferedImage img = new BufferedImage(400, 400, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        emojiLabel.paint(g2d);
        g2d.dispose();
        
        return new ImageIcon(img);
    }
    
    private void captureAndProcess() {
        if (!camera.read(frame) || frame.empty()) {
            return;
        }
        
        // Flip horizontally for mirror effect
        Core.flip(frame, frame, 1);
        
        // Convert to grayscale for detection
        Mat grayFrame = new Mat();
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(grayFrame, grayFrame);
        
        // Detect faces
        MatOfRect faces = new MatOfRect();
        faceCascade.detectMultiScale(grayFrame, faces, 1.1, 3, 
            Objdetect.CASCADE_SCALE_IMAGE, new Size(30, 30), new Size());
        
        Rect[] facesArray = faces.toArray();
        
        if (facesArray.length > 0) {
            // Process the first detected face
            Rect face = facesArray[0];
            
            // Draw rectangle around face
            Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 255, 0), 2);
            
            // Extract face region
            Mat faceROI = grayFrame.submat(face);
            
            // Detect eyes
            MatOfRect eyes = new MatOfRect();
            eyeCascade.detectMultiScale(faceROI, eyes, 1.1, 5, 
                Objdetect.CASCADE_SCALE_IMAGE, new Size(20, 20), new Size());
            
            // Detect smile
            MatOfRect smiles = new MatOfRect();
            smileCascade.detectMultiScale(faceROI, smiles, 1.8, 20, 
                Objdetect.CASCADE_SCALE_IMAGE, new Size(25, 25), new Size());
            
            // Determine expression based on detections
            String expression = determineExpression(eyes.toArray().length, smiles.toArray().length, face);
            
            if (!expression.equals(currentExpression)) {
                currentExpression = expression;
                SwingUtilities.invokeLater(() -> 
                    imageLabel.setIcon(expressionImages.get(currentExpression))
                );
            }
            
            // Draw eyes
            for (Rect eye : eyes.toArray()) {
                Point eyeCenter = new Point(face.x + eye.x + eye.width / 2, 
                                           face.y + eye.y + eye.height / 2);
                Imgproc.circle(frame, eyeCenter, (int)(eye.width * 0.5), new Scalar(255, 0, 0), 2);
            }
            
            // Draw mouth region for smile
            for (Rect smile : smiles.toArray()) {
                Point smileTopLeft = new Point(face.x + smile.x, face.y + smile.y);
                Point smileBottomRight = new Point(face.x + smile.x + smile.width, 
                                                   face.y + smile.y + smile.height);
                Imgproc.rectangle(frame, smileTopLeft, smileBottomRight, new Scalar(0, 0, 255), 2);
            }
            
            // Display current expression on video feed
            Imgproc.putText(frame, currentExpression, 
                new Point(10, 30), 
                Imgproc.FONT_HERSHEY_SIMPLEX, 
                1.0, 
                new Scalar(0, 255, 0), 
                2);
        } else {
            // No face detected, show neutral
            if (!currentExpression.equals(EXPR_NEUTRAL)) {
                currentExpression = EXPR_NEUTRAL;
                SwingUtilities.invokeLater(() -> 
                    imageLabel.setIcon(expressionImages.get(EXPR_NEUTRAL))
                );
            }
        }
        
        // Convert Mat to BufferedImage and display
        BufferedImage image = matToBufferedImage(frame);
        SwingUtilities.invokeLater(() -> {
            ImageIcon icon = new ImageIcon(image);
            cameraLabel.setIcon(icon);
        });
    }
    
    private String determineExpression(int eyeCount, int smileCount, Rect face) {
        // Simple expression detection logic
        // You can customize this logic to match your needs
        
        if (smileCount > 0) {
            return EXPR_HAPPY; // Smiling detected
        } else if (eyeCount == 1) {
            return EXPR_WINK; // Only one eye detected (could be winking)
        } else if (eyeCount == 0) {
            return EXPR_SURPRISED; // Eyes wide open or not detected
        } else if (eyeCount == 2 && smileCount == 0) {
            return EXPR_NEUTRAL;
        }
        
        return EXPR_NEUTRAL;
    }
    
    private BufferedImage matToBufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);
        
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        
        return image;
    }
    
    @Override
    public void dispose() {
        running = false;
        super.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FacialExpressionRecognizer app = new FacialExpressionRecognizer();
            app.setVisible(true);
        });
    }
}
