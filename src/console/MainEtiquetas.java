package console;
import java.util.List;

import br.usp.pcs.lta.cogroo.entity.Token;
import br.usp.pcs.lta.cogroo.entity.impl.runtime.SentenceCogroo;
import br.usp.pcs.lta.cogroo.util.viewer.CogrooWrapper;

public class MainEtiquetas {
	public static void main(String[] args) {
		CogrooWrapper cogroo = new CogrooWrapper();

		/**
		 * Lucas, a classe  está misturada no main, mas fiz rápido só para que dê para entender e fazer as etiquetas, o segredo de algumas coisas infelizmente não posso entregar hehe, mas acho que aqui tem o suficiente p/ as tags que desejam. abraço.
		 * 
		 */
		
		// Altere aqui o sumário que deseja etiquetar/classificar
		//String text = "Paciente portador de prótese mecanica mitral normofuncionante, fibrilação atrial cronica, insuficiencia tricuspide grave, insuficiencia cardiaca esquerda e direita, FE 40%, interna por dispnéia ao repouso e congestão sistemica importante. Iniciado manejo com diurético EV + HCTZ com boa reposta. Realizado cat cardiaco direito com POAP = 32 e PSAP = 36. Apresentou epistaxe recorrente com anticoagulação em níveis adequados -- avaliado pela otorrino = vasos proeminentes em plexo de Kiesselbach bilateralmente, sendo cauterizado. Tem alta hospitalar em bom estado geral, ICC classe III, com indicação de seguir acompanhamento no ambulatorio de ICT, anticoagulados e otorrino.   Orientações na alta Fazer uso das medicações conforme prescrito Restrição hídrica diaria a 1200 ml.  Dieta com pouco sal Procurar a emergencia se necessário  Medicações na alta AAS 100 mg/d Femprocumona 3 mg/d Hidralazina 25 mg 3x/d Isossorbida 40 mg 3x/d Furosemida 120 + 120 mg/d Digoxina 0,125 mg 2/2d Alopurinol 100 mg/d ";
		String text = "Feito encaminhamento para médico";

		/**
		 * pega frase para ser analisada
		 */
		String[] sentencas = cogroo.sentDetect(text);
		for (String sentenca : sentencas) {
			
			// se quiser tire os comentários daqui para ver o resultado do funcionamento, assim como os outros prints de outras funcionalidades
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
			 * aqui é uma implementação de uma função do cogroo, ajuda a obter melhor classificação no texto
			 */
			cogroo.nameFinder(sc);
			
			/** 
			 *  expande preposições, como 'na'='em'+'a'.
			 */
			
			cogroo.preTagger(sc);
			//tokens = sc.getTokens();
			//for (Token token : tokens)
			//	System.out.println(token);
			
			/**
			 * 
			 * Tagger: realiza etiquetagem morfossintática da frase. 
			 * 
			  */
			
			cogroo.tagger(sc);
			tokens = sc.getTokens();
			for (Token token : tokens)
				System.out.print(token.getLexeme() + "_" + token.getMorphologicalTag() + " ");
			System.out.println();		
		}
	}
}
