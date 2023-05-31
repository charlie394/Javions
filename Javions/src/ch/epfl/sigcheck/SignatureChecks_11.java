package ch.epfl.sigcheck;

// Attention : cette classe n'est *pas* un test JUnit, et son code n'est
// pas destiné à être exécuté. Son seul but est de vérifier, autant que
// possible, que les noms et les types des différentes entités à définir
// pour cette étape du projet sont corrects.

final class SignatureChecks_11 {
	ch.epfl.javions.gui.Main v01;
	String[] v02;
	javafx.stage.Stage v03;
	
	private SignatureChecks_11() {
	}
	
	void checkMain() throws Exception {
		v01 = new ch.epfl.javions.gui.Main();
		ch.epfl.javions.gui.Main.main( v02 );
		v01.start( v03 );
	}
}
