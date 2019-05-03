package model;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * PieceReader
 */
public class PieceReader {
	BufferedInputStream bis;

	public PieceReader(InputStream is) {
		bis = new BufferedInputStream(is);
	}

	public Piece nextPiece() {
		Piece p = new Piece();
		Coord c = new Coord();
		int red;
		try {
			while ((red = bis.read()) != -1 && !(red == '\n' && c.x == 0)) {
				if (red == '\n') {
					c.y++;
					c.x = 0;
				} else if (red == '*') {
					p.add(new Coord(c));
					c.x++;
				} else if (red == '.') {
					c.x++;
				}
			}
			if (p.isEmpty()) {
				p = null;
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}

		if (p != null) {
			p.normalize();
			Config.i().logger().info("load piece: ");
			Config.i().logger().info(p.toString());
		}

		return p;
	}

}