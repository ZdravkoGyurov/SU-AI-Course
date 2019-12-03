package naivebayesclassifier;

import java.util.Arrays;

public class Individual {

//    1. className;
//    2. handicappedInfants;
//    3. waterProjectCostSharing;
//    4. adoptionOfTheBudgetResolution;
//    5. physicianFeeFreeze;
//    6. elSalvadorAid;
//    7. religiousGroupsInSchools;
//    8. antiSateliteTestBan;
//    9. aidToNicaraguanContras;
//    10. mxMissile;
//    11. immigration;
//    12. synfuelsCorporationCutback;
//    13. educationSpending;
//    14. superfundRightToSue;
//    15. crime;
//    16. dutyFreeExports;
//    17. exportAdministrationActSouthAfrica;

    private final boolean className;
    private final boolean[] attributes;

    public Individual(final boolean className, final boolean[] attributes) {
        this.className = className;
        this.attributes = attributes;
    }

    public boolean getClassName() {
        return className;
    }

    public boolean[] getAttributes() {
        return attributes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(attributes);
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Individual)) {
            return false;
        }
        final Individual other = (Individual) obj;
        return Arrays.equals(attributes, other.attributes);
    }

}
