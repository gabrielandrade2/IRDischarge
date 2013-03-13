<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>:: PUCPR - CNPq - IRDischarge ::</title>
<link rel="stylesheet" type="text/css" href="js/jqueryui/css/blitzer/jquery-ui-1.8.21.custom.css" media="" />
<script type="text/javascript" src="js/jqueryui/js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="js/jqueryui/js/jquery-ui-1.8.21.custom.min.js"></script>
<style type="text/css">
	body{font-family: Verdana;}
	#sortable { list-style-type: none; margin: 0; padding: 0; width: 250px; }
	#sortable li { margin: 0 3px 3px 3px; padding: 0.4em; padding-left: 1.5em; font-size: 12px; height: 18px; }
	#sortable li span { position: absolute; margin-left: -1.3em; }
</style>
<script type="text/javascript">

var startLoader = function(){
	
	$('#contentResultado').html('<center>Processando textos...<br><img src="img/ajax-loader.gif"></center>');
	$( "#dialog-resultado" ).dialog( "open" );
	
}


$(document).ready(function(){
	
	//Hide/Show dos campos específicos de acordo com a origem
	$("input[name=origem]").change(function(){
		
		if($(this).val() == "excel"){
			$("#itenstexto").hide();
			$("#itensexcel").show();
		}
		else{
			$("#itenstexto").show();
			$("#itensexcel").hide();
		}
		
	});
	
	//Define ação de processamento
	$("#processa").click(function(){
		
		startLoader();
		
		var url = "servlets/ProcessaServlet";
		var arrPost = {};
		
		if($("input[name=origem]:checked").val() == "excel"){
			arrPost = { 
						origem: $("input[name=origem]:checked").val(), 
						qtde: $('#qtde').val(),
						tipo: $('input[name=tipo]:checked').val(),
						ferramenta: $('input[name=ferramenta]:checked').val()
					};
		}
		else{
			arrPost = { 
						origem: $("input[name=origem]:checked").val(), 
						texto: $('#texto').val(),
						tipo: $('input[name=tipo]:checked').val(),
						ferramenta: $('input[name=ferramenta]:checked').val()
					};
		}
		
		$.post(url, arrPost, function(data) {
			$('#contentResultado').html(data);
		});
		
	});
	
	//Define editor de regras
	$(".editor").click(function(){
		
		var type = $(this).attr('id');
		
		var salvarRegra = function(){
			
			var url = "servlets/RulesServlet";

			var arrPost = { 
							action: 'salvar',
							'type': type,
							text: $('#rule').val()
						};
			
			
			$.post(url, arrPost, function(data) {
				$( "#dialog-editor" ).dialog( "close" );
			});
			
		}
		
		$('#contentEditor').html('<center>Abrindo regras...<br><img src="img/ajax-loader.gif"></center>');
		
		var url = "servlets/RulesServlet?action=get&type=" + type;
		
		$.get(url, function(data) {
			
			$('#contentEditor').html("<textarea rows='16' cols='38' id='rule'>" + data +"</textarea>");

		});
		
		
	});
	
	$( "#dialog-resultado" ).dialog({
		autoOpen: false,
		height: 600,
		width: 800,
		modal: true,
		buttons: {
			Cancel: function() {
				$( this ).dialog( "close" );
			}
		},
		close: function() {
		}
	});
	
	$( "#tabs" ).tabs();
	
	
});

</script>
</head>
<body>
<h2>IRDischarge</h2>
<div id="tabs">
	<ul>
		<li><a href="#tabs-1">Processamento de narrativas</a></li>
		<li><a href="#tabs-2">Editor de regras</a></li>
		<li><a href="#tabs-3">Sobre o IRDischarge</a></li>
	</ul>
	<div id="tabs-1">
		
		<!-- Procedência das informações do sumário -->
		<b>Origem:</b><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="origem" value="excel">Arquivo Excel HCPA<br> 
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="origem" value="texto" checked="checked">Campo de texto 
		<br><br>
		
		<!-- Se for do arquivo XLS do HCPA, possibilita determinar quantidade -->
		<div id="itensexcel" style="display:none">
			<b>Quantidade de sumários:</b><br><input type="text" name="qtde" id="qtde" value="15" size="5" />
			<br><br>
		</div>
		
		<!-- Se for opção de inserção manual de sumário -->
		<div id="itenstexto">
			<b>Sumário de alta</b><br><textarea id="texto" cols="86" rows="8"></textarea>
			<br><br>
		</div>
		
		<!-- Define tarefa de processamento desejada -->
		<b>Tipo de processamento:</b><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" value="etiquetacao" checked="checked">Etiquetação<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" value="encaminhamento">Identificação de Continuidade<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="tipo" value="diagnostico">Identificação de Diagnóstico
		<br><br>
		
		<!-- Define ferramenta de NLP desejada -->
		<!-- 
		<b>Ferramenta:</b><span style="font-size:10px;">(válido apenas para ETIQUETAÇÃO)</span><br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="ferramenta" value="opennlp" checked="checked">OpenNLP<br>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="ferramenta" value="cogroo">Cogroo
		<br><br>
		 -->
		<!-- Define opção de visualização do resultado -->
		<b>Opção de visualização:</b><br>
		<!-- &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" name="opcao" value="download"> Download dos arquivos <br> -->
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input checked="checked" type="radio" name="opcao" value="tela"> Visualização em tela 
		<br><br>
		
		<input id="processa" type="button" value="Executar processamento"/>
		<br><br>		

	</div>
	<div id="tabs-2">
		<div id="contentEditor">
			<center>Abrindo regras...<br><img src="img/ajax-loader.gif"></center>
		</div>
	</div>
	<div id="tabs-3">
		<p>Mauris eleifend est et turpis. Duis id erat. Suspendisse potenti. Aliquam vulputate, pede vel vehicula accumsan, mi neque rutrum erat, eu congue orci lorem eget lorem. Vestibulum non ante. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Fusce sodales. Quisque eu urna vel enim commodo pellentesque. Praesent eu risus hendrerit ligula tempus pretium. Curabitur lorem enim, pretium nec, feugiat nec, luctus a, lacus.</p>
		<p>Duis cursus. Maecenas ligula eros, blandit nec, pharetra at, semper at, magna. Nullam ac lacus. Nulla facilisi. Praesent viverra justo vitae neque. Praesent blandit adipiscing velit. Suspendisse potenti. Donec mattis, pede vel pharetra blandit, magna ligula faucibus eros, id euismod lacus dolor eget odio. Nam scelerisque. Donec non libero sed nulla mattis commodo. Ut sagittis. Donec nisi lectus, feugiat porttitor, tempor ac, tempor vitae, pede. Aenean vehicula velit eu tellus interdum rutrum. Maecenas commodo. Pellentesque nec elit. Fusce in lacus. Vivamus a libero vitae lectus hendrerit hendrerit.</p>
	</div>
</div>

<br/>
<%= new java.util.Date().toString()  %>


<div id="dialog-resultado" title="Resultado do processamento" style="display:none">
	<div id="contentResultado">
		<center>Processando textos...<br><img src="img/ajax-loader.gif"></center>
	</div>
</div>


</body>
</html>