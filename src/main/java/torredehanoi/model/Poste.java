package torredehanoi.model;

import java.util.Stack;
import java.util.List;

public class Poste {

    private final int idPoste;

    // Stack para forzar UEPS/LIFO
    private final Stack<Disco> discos;

    public Poste(int idPoste) {
        this.idPoste = idPoste;
        this.discos = new Stack<>();
    }

    public boolean colocarDisco(Disco nuevoDisco) {

        if (!discos.isEmpty()) {
            Disco discoSuperior = discos.peek();

            if (nuevoDisco.getTamano() > discoSuperior.getTamano()) {
                return false;
            }
        }

        discos.push(nuevoDisco);
        return true;
    }

    public Disco retirarDisco() {
        if (discos.isEmpty()) {
            return null;
        }
        return discos.pop();
    }

    public Disco getDiscoSuperior() {
        if (discos.isEmpty()) {
            return null;
        }
        return discos.peek();
    }

    public List<Disco> getDiscos() {
        return discos;
    }
}