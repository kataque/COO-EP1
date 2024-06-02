import java.awt.*;
import java.util.Random;

/**
	Esta classe representa a bola usada no jogo. A classe princial do jogo (Pong)
	instancia um objeto deste tipo quando a execução é iniciada.
*/

public class Ball {

	double cx, cy, width, height, speed;
	Color color;

	public enum Direction {
		UP_RIGHT, UP_LEFT, DOWN_RIGHT, DOWN_LEFT
	}

	public enum CollisionPosition {
		UPPER_HALF, LOWER_HALF
	}

	Direction direction = Direction.UP_LEFT;

	CollisionPosition collisionPosition;
	/**
		Construtor da classe Ball. Observe que quem invoca o construtor desta classe define a velocidade da bola 
		(em pixels por millisegundo), mas não define a direção deste movimento. A direção do movimento é determinada 
		aleatóriamente pelo construtor.

		@param cx coordenada x da posição inicial da bola (centro do retangulo que a representa).
		@param cy coordenada y da posição inicial da bola (centro do retangulo que a representa).
		@param width largura do retangulo que representa a bola.
		@param height altura do retangulo que representa a bola.
		@param color cor da bola.
		@param speed velocidade da bola (em pixels por millisegundo).
	*/

	public Ball(double cx, double cy, double width, double height, Color color, double speed){

		this.cx = cx;
		this.cy = cy;
		this.width = width;
		this.height = height;
		this.color = color;
		this.speed = speed;
	}

	/**
		Método chamado sempre que a bola precisa ser (re)desenhada.
	*/

	public void draw(){

		GameLib.setColor(color);
		GameLib.fillRect(cx, cy, width, height);
	}

	/**
		Método chamado quando o estado (posição) da bola precisa ser atualizado.
		
		@param delta quantidade de millisegundos que se passou entre o ciclo anterior de atualização do jogo e o atual.
	*/

	public void update(long delta){

		switch (direction){
			case UP_LEFT:
				cx -= (speed * Math.sqrt(3)/2 * delta);
				cy -= (speed * 1/2 * delta);
				break;
			case UP_RIGHT:
				cx += (speed * Math.sqrt(3)/2 * delta);
				cy -= (speed * 1/2 * delta);
				break;
			case DOWN_LEFT:
				cx -= (speed * Math.sqrt(3)/2 * delta);
				cy += (speed * 1/2 * delta);
				break;
			case DOWN_RIGHT:
				cx += (speed * Math.sqrt(3)/2 * delta);
				cy += (speed * 1/2 * delta);
				break;
		}
	}

	/**
		Método chamado quando detecta-se uma colisão da bola com um jogador.
	
		@param playerId uma string cujo conteúdo identifica um dos jogadores.
	*/

	public void onPlayerCollision(String playerId){

		switch(playerId){
			case "Player 1":
				if((direction == Direction.UP_LEFT && collisionPosition == CollisionPosition.LOWER_HALF) || (direction == Direction.DOWN_LEFT && collisionPosition == CollisionPosition.LOWER_HALF))
					direction = Direction.DOWN_RIGHT;
				if((direction == Direction.UP_LEFT && collisionPosition == CollisionPosition.UPPER_HALF) || (direction == Direction.DOWN_LEFT && collisionPosition == CollisionPosition.UPPER_HALF))
					direction = Direction.UP_RIGHT;
				break;

			case "Player 2":
				if((direction == Direction.UP_RIGHT && collisionPosition == CollisionPosition.LOWER_HALF) || (direction == Direction.DOWN_RIGHT && collisionPosition == CollisionPosition.LOWER_HALF))
					direction = Direction.DOWN_LEFT;
				if((direction == Direction.UP_RIGHT && collisionPosition == CollisionPosition.UPPER_HALF) || (direction == Direction.DOWN_RIGHT && collisionPosition == CollisionPosition.UPPER_HALF))
					direction = Direction.UP_LEFT;
				break;
		}

	}

	/**
		Método chamado quando detecta-se uma colisão da bola com uma parede.

		@param wallId uma string cujo conteúdo identifica uma das paredes da quadra.
	*/

	public void onWallCollision(String wallId){

		switch(wallId){
			case "Top":
				if (direction == Direction.UP_LEFT){
					direction = Direction.DOWN_LEFT;
				}
				else {
					direction = Direction.DOWN_RIGHT;
				}
				break;

			case "Bottom":
				if (direction == Direction.DOWN_LEFT){
					direction = Direction.UP_LEFT;
				}
				else {
					direction = Direction.UP_RIGHT;
				}
				break;

			case "Left":
				if (direction == Direction.DOWN_LEFT){
					direction = Direction.DOWN_RIGHT;
				}
				else {
					direction = Direction.UP_RIGHT;
				}
				break;

			case "Right":
				if (direction == Direction.DOWN_RIGHT){
					direction = Direction.DOWN_LEFT;
				}
				else {
					direction = Direction.UP_LEFT;
				}
				break;

		}

	}

	/**
		Método que verifica se houve colisão da bola com uma parede.

		@param wall referência para uma instância de Wall contra a qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/
	
	public boolean checkCollision(Wall wall) {

		String wallId = wall.getId();

		switch (wallId) {
			case "Top":
				if (cy - height/2 <= wall.getCy() + wall.getHeight()/2) {
					return true;
				}
				break;


			case "Bottom":
				if (cy + height/2 >= wall.getCy() - wall.getHeight()/2) {
					return true;
				}
				break;


			case "Left":
				if (cx - width/2 <= wall.getCx() + wall.getWidth()/2) {
					return true;
				}
				break;


			case "Right":
				if (cx + width/2 >= wall.getCx() - wall.getWidth()/2) {
					return true;
				}
				break;
		}
		return false;
	}
	/**
		Método que verifica se houve colisão da bola com um jogador.

		@param player referência para uma instância de Player contra o qual será verificada a ocorrência de colisão da bola.
		@return um valor booleano que indica a ocorrência (true) ou não (false) de colisão.
	*/	

	public boolean checkCollision(Player player){

		String playerId = player.getId();

		switch(playerId){

			case "Player 1":
				if (cx - width/2 <= player.getCx() + player.getWidth()/2 && player.getCy() + player.getHeight()/2 >= cy && cy >= player.getCy() - player.getHeight()/2 && (direction == Direction.UP_LEFT || direction == Direction.DOWN_LEFT)) {
					if (cy > player.getCy()){
						collisionPosition = CollisionPosition.LOWER_HALF;
					} else {
						collisionPosition = CollisionPosition.UPPER_HALF;
					}
					return true;
				}
				break;

			case "Player 2":
				if (cx + width/2 >= player.getCx() - player.getWidth()/2 && player.getCy() + player.getHeight()/2 >= cy && cy >= player.getCy() - player.getHeight()/2 && (direction == Direction.UP_RIGHT || direction == Direction.DOWN_RIGHT)) {
					if (cy > player.getCy()){
						collisionPosition = CollisionPosition.LOWER_HALF;
					} else {
						collisionPosition = CollisionPosition.UPPER_HALF;
					}
					return true;
				}
				break;

		}
		return false;
	}

	/**
		Método que devolve a coordenada x do centro do retângulo que representa a bola.
		@return o valor double da coordenada x.
	*/
	
	public double getCx(){

		return cx;
	}

	/**
		Método que devolve a coordenada y do centro do retângulo que representa a bola.
		@return o valor double da coordenada y.
	*/

	public double getCy(){

		return cy;
	}

	/**
		Método que devolve a velocidade da bola.
		@return o valor double da velocidade.

	*/

	public double getSpeed(){

		return speed;
	}

	/**
	 Método que devolve uma direção aleatória da bola.
	 @return o enum da direção.

	 */

	public Direction randomDirection() {
		int pick = new Random().nextInt(Direction.values().length);
		return Direction.values()[pick];
	}

}
