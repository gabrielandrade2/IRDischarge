package console;
import java.util.List;

import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.SentenceCogroo;
import br.usp.pcs.lta.cogroo.util.viewer.CogrooWrapper;

public class MainEtiquetas {
	public static void main(String[] args) {
		CogrooWrapper cogroo = new CogrooWrapper();

		/**
		 * Lucas, a classe  est� misturada no main, mas fiz r�pido s� para que d� para entender e fazer as etiquetas, o segredo de algumas coisas infelizmente n�o posso entregar hehe, mas acho que aqui tem o suficiente p/ as tags que desejam. abra�o.
		 * 
		 */
		
		// Altere aqui o sum�rio que deseja etiquetar/classificar
		//String text = "Paciente portador de pr�tese mecanica mitral normofuncionante, fibrila��o atrial cronica, insuficiencia tricuspide grave, insuficiencia cardiaca esquerda e direita, FE 40%, interna por dispn�ia ao repouso e congest�o sistemica importante. Iniciado manejo com diur�tico EV + HCTZ com boa reposta. Realizado cat cardiaco direito com POAP = 32 e PSAP = 36. Apresentou epistaxe recorrente com anticoagula��o em n�veis adequados -- avaliado pela otorrino = vasos proeminentes em plexo de Kiesselbach bilateralmente, sendo cauterizado. Tem alta hospitalar em bom estado geral, ICC classe III, com indica��o de seguir acompanhamento no ambulatorio de ICT, anticoagulados e otorrino.   Orienta��es na alta Fazer uso das medica��es conforme prescrito Restri��o h�drica diaria a 1200 ml.  Dieta com pouco sal Procurar a emergencia se necess�rio  Medica��es na alta AAS 100 mg/d Femprocumona 3 mg/d Hidralazina 25 mg 3x/d Isossorbida 40 mg 3x/d Furosemida 120 + 120 mg/d Digoxina 0,125 mg 2/2d Alopurinol 100 mg/d ";
		String text = "Feito encaminhamento para m�dico";

		/**
		 * pega frase para ser analisada
		 */
		String[] sentencas = cogroo.sentDetect(text);
		for (String sentenca : sentencas) {
			
			// se quiser tire os coment�rios daqui para ver o resultado do funcionamento, assim como os outros prints de outras funcionalidades
			//System.out.println(sentenca);
			
			
			/**
			 * usa o tokenizer para fatiar o texto
			 */
			SentenceCogroo sc = new SentenceCogroo(sentenca);
			List<Token> tokens = null;
			
			
			cogroo.tokenizer(sc);
			//tokens = sc.getTokens();
			//for (Token token : tokens)
			//	System.out.println(token);
			
			/**
			 * aqui � uma implementa��o de uma fun��o do cogroo, ajuda a obter melhor classifica��o no texto
			 */
			cogroo.nameFinder(sc);
			
			/** 
			 *  expande preposi��es, como 'na'='em'+'a'.
			 */
			
			cogroo.preTagger(sc);
			//tokens = sc.getTokens();
			//for (Token token : tokens)
			//	System.out.println(token);
			
			/**
			 * 
			 * Tagger: realiza etiquetagem morfossint�tica da frase. 
			 * 
			  */
			
			cogroo.tagger(sc);
			tokens = sc.getTokens();
			for (Token token : tokens)
				System.out.print(/*token.getLexeme() +*/ "_" + token.getMorphologicalTag() + " ");
			System.out.println();		
		}
	}
}
