import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;

//void rotate (double theta) - выполнить поворот на theta радиан;
//void scale (double sx, double sy) - выполнить масштабирование с коэффициентами sx, sy по оси Ох и Оу, соответственно;
//void shear (double shx, double shy) - выполнить сдвиг с коэффи­циентами shx, shy;
//void translate (double tx, double ty) - выполнить параллельный перенос на вектор (tx, ty).

public class Graphic {
	public static void DrawAngledText(Graphics gr, String text, float angle) {
		Graphics2D gr2d=(Graphics2D)gr;
		AffineTransform at = gr2d.getTransform();
		double cos=Math.cos(angle), sin=Math.sin(angle); 
		AffineTransform rotate = new AffineTransform(cos,-sin,sin,cos,100,100);
		gr2d.setTransform(rotate);
		gr2d.drawString(text, 0, 0);
		gr2d.setTransform(at);
	}
}

//public class Flower extends java.awt.Canvas { 
//	private int count; 
//	public Flower(int count) {
//		this.count = count;
//	}
// 
//	public void paint(Graphics gr) {
//		super.paint(gr);
//		Graphics2D gr2d=(Graphics2D) gr;
//		int size=Math.min(getWidth(), getHeight())/2; 
//		gr2d.translate(size, size);
//		for(int i=0;i<=count; count++) {
//			gr2d.fillOval(0, -size/6, size, size/3); 
//			gr2d.rotate(2*Math.PI/count);
//		}
//	}
//}



//Создание линий различных типов
	//Stroke stroke = new BasicStroke(2.5); gr2d.setStroke(stroke);


//Заливки
	//Color - обеспечивает одноцветную закраску замкнутой области.
	//TexturePaint - обеспечивает заполнение замкнутой области узором (текстурой).
	//GradientPaint - обеспечивает плавное изменение цвета вдоль направле­ния.
	
	//Paint paint = new GradientPaint(x, у, Color.RED, x+width, у, Color.BLUE);
	//gr2d.setPaint(paint);

//Проверка на принадлежность к  
//if (gr instanceof Graphics2D) {
//	Graphics2D gr2d=(Graphics2D)gr;
//}


//Вывод текста
//Font font = new Font("Comic Sans MS",Font.BOLD,20);
//gr.setFont(font);
//gr.setColor(Color.RED);
//gr.drawString("Hello, world!",100,100);


