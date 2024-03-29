/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 *
 * @author u17168
 */
public class ClienteProjeto1 {
    private static final String URL_ROOT = "http://127.0.0.1:8080/Projeto_Restful_AD/webresources/aluno/";
    private static final String DELETE_ALUNO = "deleteAluno";
    private static final String INCLUDE_ALUNO = "includeAluno";
    private static final String PUT_ALUNO = "putAluno";
    private static final String GET_ALUNO_NOME = "getAlunoNome/{nome}";
    private static final String GET_ALUNO_RA = "getAlunoRA/{ra}";
    private static final String GET_ALL_ALUNOS = "getAllAlunos";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        ClienteProjeto1 cp = new ClienteProjeto1();
        
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        Gson gson = new Gson();
        
        String input_string;
        do {
            System.out.println("==============================="); 
            System.out.println("=1-=Delete Aluno==============="); 
            System.out.println("=2-=Include Aluno=============="); 
            System.out.println("=3-=Put Aluno (alterar)========"); 
            System.out.println("=4-=Get Aluno por Nome========="); 
            System.out.println("=5-=Get Aluno por RA==========="); 
            System.out.println("=6-=Listar Alunos==============");
            System.out.println("=STOP-=Parar programa==========");
            System.out.println("==============================="); 

            String content, response;
            switch (input_string = input.readLine().toLowerCase()) {
                case "1":
                    System.out.println("RA: ");
                    content = input.readLine();
                    response = cp.sendRequest(URL_ROOT + DELETE_ALUNO, POST, content);
                    System.out.println(response);
                    break;
                case "2":
                    content = getAlunoFromCli(input, gson);
                    response = cp.sendRequest(URL_ROOT + INCLUDE_ALUNO, POST, content);
                    System.out.println(response);
                    break;
                case "3":
                    content = getAlunoFromCli(input, gson);
                    response = cp.sendRequest(URL_ROOT + PUT_ALUNO, PUT, content);
                    System.out.println(response);
                    break;
                case "4":
                    System.out.println("Nome: ");
                    content = input.readLine();
                    response = cp.sendRequest(URL_ROOT + GET_ALUNO_NOME.replace("{nome}", content), GET, null);
                    System.out.println(response);
                    break;
                case "5":
                    System.out.println("RA: ");
                    content = input.readLine();
                    response = cp.sendRequest(URL_ROOT + GET_ALUNO_RA.replace("{ra}", content), GET, null);
                    System.out.println(response);
                    break;
                case "6":
                    response = cp.sendRequest(URL_ROOT + GET_ALL_ALUNOS, GET, null);
                    
                    Aluno[] alunos = gson.fromJson(response, Aluno[].class);
                    
                    if (alunos.length == 0) {
                        System.out.println("Nenhum aluno cadastrado");
                    }
                    else
                    for (Aluno a : alunos) {
                        System.out.println(a.toString());
                    }
                    
                    break;
            }

        } while (!input_string.toLowerCase().equals("stop"));
    }
    
    private String sendRequest(String url, String method, String content) throws MalformedURLException, ProtocolException, IOException{
        URL objURL = new URL(url);
        
        HttpURLConnection con = (HttpURLConnection) objURL.openConnection();
        
        con.setRequestMethod(method);
       
        System.out.println("\nEnviando requisição '" + method + "' para URL: " + url);
        if (content != null) {
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(content.getBytes());
        }
        
        int responseCode = con.getResponseCode();
        System.out.println("Response Code: "+ responseCode);
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((con.getInputStream())));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null){
            response.append(inputLine);
            }
            br.close();
            
            con.disconnect();
            return response.toString();
        }
        catch (IOException e) {
            //e.printStackTrace();
            return "Servidor retornou erro na requisição";
        }
    }
    
    private static String getAlunoFromCli(BufferedReader input, Gson gson) throws Exception {
        System.out.println("Forneça um aluno");
        System.out.print("Nome: ");
        
        String nome = input.readLine();
        
        System.out.print("RA: ");
        
        String RA = input.readLine();
        
        System.out.print("Email: ");
        
        String email = input.readLine();
        
        return gson.toJson(new Aluno(RA, nome, email));
    }
        
}    

