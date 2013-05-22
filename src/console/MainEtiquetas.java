package console;
import java.util.List;

import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.SentenceCogroo;
import br.usp.pcs.lta.cogroo.util.viewer.CogrooWrapper;

public class MainEtiquetas {
	public static void main(String[] args) {
		CogrooWrapper cogroo = new CogrooWrapper();

		/**
		 * Lucas, a classe  estï¿½ misturada no main, mas fiz rï¿½pido sï¿½ para que dï¿½ para entender e fazer as etiquetas, o segredo de algumas coisas infelizmente nï¿½o posso entregar hehe, mas acho que aqui tem o suficiente p/ as tags que desejam. abraï¿½o.
		 * 
		 */
		
		
		// Altere aqui o sumï¿½rio que deseja etiquetar/classificar
		//String text = "Paciente portador de prï¿½tese mecanica mitral normofuncionante, fibrilaï¿½ï¿½o atrial cronica, insuficiencia tricuspide grave, insuficiencia cardiaca esquerda e direita, FE 40%, interna por dispnï¿½ia ao repouso e congestï¿½o sistemica importante. Iniciado manejo com diurï¿½tico EV + HCTZ com boa reposta. Realizado cat cardiaco direito com POAP = 32 e PSAP = 36. Apresentou epistaxe recorrente com anticoagulaï¿½ï¿½o em nï¿½veis adequados -- avaliado pela otorrino = vasos proeminentes em plexo de Kiesselbach bilateralmente, sendo cauterizado. Tem alta hospitalar em bom estado geral, ICC classe III, com indicaï¿½ï¿½o de seguir acompanhamento no ambulatorio de ICT, anticoagulados e otorrino.   Orientaï¿½ï¿½es na alta Fazer uso das medicaï¿½ï¿½es conforme prescrito Restriï¿½ï¿½o hï¿½drica diaria a 1200 ml.  Dieta com pouco sal Procurar a emergencia se necessï¿½rio  Medicaï¿½ï¿½es na alta AAS 100 mg/d Femprocumona 3 mg/d Hidralazina 25 mg 3x/d Isossorbida 40 mg 3x/d Furosemida 120 + 120 mg/d Digoxina 0,125 mg 2/2d Alopurinol 100 mg/d ";
		String text = "Feito encaminhamento para mï¿½dico";

		/**
		 * pega frase para ser analisada
		 */
		String[] sentencas = cogroo.sentDetect(text);
		for (String sentenca : sentencas) {
			
			// se quiser tire os comentï¿½rios daqui para ver o resultado do funcionamento, assim como os outros prints de outras funcionalidades
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
			 * aqui ï¿½ uma implementaï¿½ï¿½o de uma funï¿½ï¿½o do cogroo, ajuda a obter melhor classificaï¿½ï¿½o no texto
			 */
			cogroo.nameFinder(sc);
			
			/** 
			 *  expande preposiï¿½ï¿½es, como 'na'='em'+'a'.
			 */
			
			cogroo.preTagger(sc);
			//tokens = sc.getTokens();
			//for (Token token : tokens)
			//	System.out.println(token);
			
			/**
			 * 
			 * Tagger: realiza etiquetagem morfossintï¿½tica da frase. 
			 * 
			  */
			
			cogroo.tagger(sc);
			/**
			* CHUNKER: Realiza etiquetagem sintática da frase
			*/
			cogroo.chunker(sc);
			 
			tokens = sc.getTokens();
			for (Token token : tokens)
			System.out.print(token.getLexeme() + "_" + token.getMorphologicalTag() + " - (" + token.getChunkTag() + ")");
			//System.out.print(token.getLexeme() + "_" + token.getMorphologicalTag() + " ");
			System.out.println(); 		
		}
	}
}
