package lu.pistache.overconcentrated;

import lu.pistache.overconcentrated.model.PortfolioEntry;
import lu.pistache.overconcentrated.model.Security;
import org.junit.jupiter.api.Test;

import java.util.*;

public class CalibrateMarginsTest extends AbstractDataTest {
    @Test
    public void calibrateMarginsTest() {
        HashMap<String, PortfolioEntry> portfolioEntries = new HashMap<>();
        portfolioEntries.put("ETR:VOW3", new PortfolioEntry(1, 0.9));
        portfolioEntries.put("IPA:AIR", new PortfolioEntry(1, 0.9));
        portfolioEntries.put("ETR:LHA", new PortfolioEntry(1, 0.9));
        //portfolioEntries.put("ETR:SGM", new PortfolioEntry(1, 0.9));
        Security vow3 = new Security("ETR:VOW3", 20.0, 0.9);
        Security air = new Security("IPA:AIR", 20.0, 0.9);
        Security lha = new Security("ETR:LHA", 200.0, 0.9);
        Security sgm = new Security("ETR:SGM", 230.0, 0.9);

        Map<String, Security> securities = new HashMap<String, Security>();
        securities.put(vow3.getName(), vow3);
        securities.put(air.getName(), air);
        securities.put(lha.getName(), lha);
        securities.put(sgm.getName(), sgm);

        //portfolio value
        double portfolioValue = 0;


        //create a portfolio
        //todo: the value of a security should not be part of a portfolio.
        for (String securityNameInPortfolio : portfolioEntries.keySet()) {
            PortfolioEntry portfolioEntry = portfolioEntries.get(securityNameInPortfolio);
            Security security = securities.get(securityNameInPortfolio);
            double value = portfolioEntry.getQuantity() * security.getValue();
            portfolioEntry.setValue(value);
            portfolioValue += value;
        }


        //calculate concentration
        resetConcentration(portfolioEntries, securities);

        //sort out overconcentrated
        List<PortfolioEntry> overconcentratedEntries = new ArrayList<>();
        for (String securityNameInPortfolio : portfolioEntries.keySet()) {
            PortfolioEntry portfolioEntry = portfolioEntries.get(securityNameInPortfolio);
            if (portfolioEntry.getPortfolioConcentration() > 0.30) {
                overconcentratedEntries.add(portfolioEntry);
            }
        }

        double portfolioHaircutValue = calculateHaircutValue(portfolioEntries.values());
        double portfolioOverconcentratedHaircutValue = calculateHaircutValue(overconcentratedEntries);
        double portfolioHaircutValueWithoutOverconcentrateds = portfolioHaircutValue - portfolioOverconcentratedHaircutValue;



        if (overconcentratedEntries.size() > 0) {
            double cumulativeConcentrationOfOverconcentrateds = overconcentratedEntries.size() * 0.3;
            double cumulativeConcentrationOfNonOverconcentrateds = 1.0 - cumulativeConcentrationOfOverconcentrateds;
            double targetDecreasedValueOfOverconcentrated = (portfolioHaircutValueWithoutOverconcentrateds * cumulativeConcentrationOfOverconcentrateds / cumulativeConcentrationOfNonOverconcentrateds) / overconcentratedEntries.size();

            for (PortfolioEntry overconcentratedEntry : overconcentratedEntries) {
                double newMargin = overconcentratedEntry.getMargin() * targetDecreasedValueOfOverconcentrated / overconcentratedEntry.getHaircutValue();
                overconcentratedEntry.setMargin(newMargin);
            }
            resetConcentration(portfolioEntries, securities);
            printPortfolios(portfolioEntries);
        }


    }

    private double calculateHaircutValue(Collection<PortfolioEntry> entries) {
        double value = 0;
        for (PortfolioEntry portfolioEntry : entries) {
            value += portfolioEntry.getHaircutValue();
        }
        return value;
    }

    private double calculateTotalHaircutValue(HashMap<String, PortfolioEntry> portfolioEntries) {
        double portfolioHaircutValue = 0;
        for (PortfolioEntry portfolioEntry : portfolioEntries.values()) {
            portfolioHaircutValue += portfolioEntry.getHaircutValue();
        }
        return portfolioHaircutValue;
    }

    private double calculatePortfolioValueAfterMargin(HashMap<String, PortfolioEntry> portfolioEntries, Map<String, Security> securities) {
        double portfolioHaircutValue = 0;
        for (String securityNameInPortfolio : portfolioEntries.keySet()) {
            PortfolioEntry portfolioEntry = portfolioEntries.get(securityNameInPortfolio);
            Security security = securities.get(securityNameInPortfolio);
            double haircutValue = portfolioEntry.getQuantity() * security.getValue() * portfolioEntry.getMargin();
            portfolioEntry.setHaircutValue(haircutValue);
            portfolioHaircutValue += haircutValue;
        }
        return portfolioHaircutValue;
    }

    private void setHaircutValue(PortfolioEntry entry) {
        entry.setHaircutValue(entry.getMargin() * entry.getValue());
    }

    private void resetConcentration(HashMap<String, PortfolioEntry> portfolioEntries, Map<String, Security> securities) {
        double portfolioHaircutValue = calculatePortfolioValueAfterMargin(portfolioEntries, securities);
        for (String securityNameInPortfolio : portfolioEntries.keySet()) {
            PortfolioEntry portfolioEntry = portfolioEntries.get(securityNameInPortfolio);
            portfolioEntry.setPortfolioConcentration(portfolioEntry.getHaircutValue() / portfolioHaircutValue);
        }
    }

}
