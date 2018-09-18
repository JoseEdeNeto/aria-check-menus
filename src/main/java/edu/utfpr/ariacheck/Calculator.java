package edu.utfpr.ariacheck;

import java.util.List;

public class Calculator {
    
    public double calculaMedia(List<Double> vetor){
        if(vetor.isEmpty())
            return 0.0;
        double media = 0.0;
        for (double valor : vetor){
            media += valor;
        }
        media /= (double)vetor.size();
        return media;
    }
    
    
    public double calculaDesvioPadrao(List<Double> vetor){
        if(vetor.isEmpty())
            return 0.0;
        double sum = 0;
        double media = calculaMedia(vetor);
        for(double valor : vetor){
            double aux = valor - media;
            sum += aux * aux;
        }
        return sum <= 0 ? 0 : Math.sqrt(((double) 1 / (vetor.size() - 1)) * sum);
    }
    
}
