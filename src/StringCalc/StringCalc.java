/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StringCalc;

import java.util.StringTokenizer;

/**
 *
 * @author ymichot
 */
public class StringCalc {

    private static String sep = " == ";

    public void StringCalc() {
    }

    public String doCalculate(String text, boolean verbose, boolean metric) {
        double len = 27 * 2.54;
        double totalTension = 0.;
        StringBuilder out = new StringBuilder();
        StringTokenizer lines = new StringTokenizer(text, "\n", true);
        while (lines.hasMoreTokens()) {
            String line = lines.nextToken();
            if (line.charAt(0) == '\n') {
                out.append(line);
                continue;
            }
            line = strip(line);
            StringTokenizer words = new StringTokenizer(line);
            String ww = words.nextToken();
            if (ww.endsWith(":")) {
                ww = ww.substring(1, ww.length() - 1);
            }
            if (ww.equalsIgnoreCase("len") || ww.equalsIgnoreCase("length")) {
                try {
                    out.append(line);
                    len = parseLength(words, metric);
                    double rlen = Math.round((metric ? len : len / 2.54) * 100) / 100.;
                    if (verbose) {
                        out.append(sep).append(rlen).append(metric ? "cm" : "\"");
                    }
                } catch (Exception ee) {
                    out.append(line).append(sep).append("bad length: ").append(ee);
                }
                continue;
            }
            if (ww.equalsIgnoreCase("total")) {
                out.append(line).append(sep).append(Math.round(totalTension * 100) / 100.).append(metric ? "kg" : "#");
                totalTension = 0.;
                continue;
            }
            if (ww.equalsIgnoreCase("totalX2")) {
                out.append(line).append(sep).append(Math.round(totalTension * 2 * 100) / 100.).append(metric ? "kg" : "#");
                totalTension = 0.;
                continue;
            } else {
                int note;
                double tension = 0.;
                double gauge = 0.;
                boolean doTension = true;
                double[][] weights;

                try {
                    note = parseNote(ww);
                } catch (Exception ee) {
                    out.append(line);
                    continue;
                }

                try {
                    ww = words.nextToken();
                    if (ww.endsWith(".")) {
                        ww = ww.substring(0, ww.length() - 1);
                    }
                    if (ww.endsWith("s")) {
                        ww = ww.substring(0, ww.length() - 1);
                    }
                    doTension = !(ww.endsWith("#") || ww.endsWith("lb") || ww.endsWith("kg"));
                    if (doTension) {
                        gauge = parseGauge(ww, metric);
                        if (gauge < .004) {
                            out.append(line).append(sep).append("guage too small: ").append(Math.round(gauge * 10000) / 10000.).append("\"");
                            continue;
                        }
                    } else {
                        tension = parseTension(ww, metric);
                    }
                } catch (Exception ee) {
                    out.append(line).append(sep).append("bad ").append(doTension ? "gauge" : "tension").append(": ").append(ee);
                    continue;
                }

                try {
                    ww = words.nextToken();
                    if (ww.equalsIgnoreCase("ckplb")) {
                        weights = CKPLB;
                    } else if (ww.equalsIgnoreCase("ckwnb")) {
                        weights = CKWNB;
                    } else if (ww.equalsIgnoreCase("ckplg")) {
                        weights = CKPLG;
                    } else if (ww.equalsIgnoreCase("ckwng")) {
                        weights = CKWNG;
                    } else if (ww.equalsIgnoreCase("dapl")) {
                        weights = DAPL;
                    } else if (ww.equalsIgnoreCase("dapb")) {
                        weights = DAPB;
                    } else if (ww.equalsIgnoreCase("daxs")) {
                        weights = DAXSG;
                    } else if (ww.equalsIgnoreCase("danw")) {
                        weights = DANW;
                    } else if (ww.equalsIgnoreCase("dahr")) {
                        weights = DAHRG;
                    } else if (ww.equalsIgnoreCase("dacg")) {
                        weights = DACG;
                    } else if (ww.equalsIgnoreCase("daft")) {
                        weights = DAFT;
                    } else if (ww.equalsIgnoreCase("dabw")) {
                        weights = DABW;
                    } else if (ww.equalsIgnoreCase("dazw")) {
                        weights = DAZW;
                    } else if (ww.equalsIgnoreCase("daxb")) {
                        weights = DAXB;
                    } else if (ww.equalsIgnoreCase("dahb")) {
                        weights = DAHB;
                    } else if (ww.equalsIgnoreCase("dabc")) {
                        weights = DABC;
                    } else if (ww.equalsIgnoreCase("dabs")) {
                        weights = DABS;
                    } else {
                        throw (new IllegalArgumentException());
                    }
                } catch (Exception ee) {
                    out.append(line).append(sep).append("bad string type: ").append(ee);
                    continue;
                }
                double fq = 440. * Math.pow(Math.pow(2., 1. / 12.), note);
                if (doTension) {
                    double wt = getWeight(gauge, weights);
                    tension = (4 * fq * fq * len * (len * wt)) / 980621.;
                    if (!metric) {
                        tension *= 2.2047;
                    }
                    totalTension += tension;
                    tension = Math.round(tension * 100) / 100.;
                    fq = Math.round(fq * 10) / 10.;
                    wt = Math.round(wt * 10000) / 10000.;
                    out.append(line).append(sep).append(tension).append(metric ? "kg" : "#");
                    if (verbose) {
                        out.append("  ( ").append(wt).append("gm/cm ").append(fq).append("hz )");
                    }
                } else {
                    double wt = (tension * 980621.) / (4 * fq * fq * len * len);
                    gauge = getGauge(wt, weights);
                    if (metric) {
                        gauge *= 25.4;
                    }
                    gauge = Math.round(gauge * 10000) / 10000.;
                    fq = Math.round(fq * 10) / 10.;
                    wt = Math.round(wt * 10000) / 10000.;
                    out.append(line).append(sep).append(gauge).append(metric ? "mm" : "\"");
                    if (verbose) {
                        out.append("  ( ").append(wt).append("gm/cm ").append(fq).append("hz )");
                    }
                }
            }
        }
        return out.toString();
    }

    private String strip(String ss) {
        int ndx = ss.lastIndexOf(sep);
        if (ndx >= 0) {
            return ss.substring(0, ndx);
        } else {
            return ss;
        }
    }

    private static double getWeight(double gauge, double[][] weights) {
        return getGaugeOrWeight(gauge, weights, 0, 1) / (2.2046 / 1000) / 2.54;
    }

    private static double getGauge(double weight, double[][] weights) {
        return getGaugeOrWeight(weight * (2.2046 / 1000) * 2.54, weights, 1, 0);
    }

    private static double getGaugeOrWeight(double value, double[][] weights, int inndx, int outndx) {
        int ll = weights.length;
        int ii;

        for (ii = 0; ii < ll; ++ii) {
            if (value < weights[ ii][ inndx]) {
                break;
            }
        }
        double wt;
        if (ii == ll) {
            wt = weights[ ll - 3][ outndx]
                    + ((weights[ ll - 1][ outndx] - weights[ ll - 3][ outndx])
                    * (value - weights[ ll - 3][ inndx])
                    / (weights[ ll - 1][ inndx] - weights[ ll - 3][ inndx]));
        } else if (ii == 0) {
            wt = weights[ 0][ outndx]
                    + ((weights[ 2][ outndx] - weights[ 0][ outndx])
                    * (value - weights[ 0][ inndx])
                    / (weights[ 2][ inndx] - weights[ 0][ inndx]));
        } else {
            wt = weights[ ii - 1][ outndx]
                    + ((weights[ ii][ outndx] - weights[ ii - 1][ outndx])
                    * (value - weights[ ii - 1][ inndx])
                    / (weights[ ii][ inndx] - weights[ ii - 1][ inndx]));
        }
        return wt;
    }

    private static double parseGauge(String ww, boolean metric) {
        int ii;
        for (ii = 0; ii < ww.length(); ++ii) {
            char cc = ww.charAt(ii);
            if (cc != '.' && !Character.isDigit(cc)) {
                break;
            }
        }
        double dd = new Double(ww.substring(0, ii)).doubleValue();
        if (ii < ww.length()) {
            ww = ww.substring(ii);
            if (ww.equals("\"")) {
            } else if (ww.equals("cm")) {
                dd /= 2.54;
            } else if (ww.equals("mm")) {
                dd /= 25.4;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            if (metric) {
                dd /= 25.4;
            }
        }

        return dd;
    }

    private static double parseTension(String ww, boolean metric) {
        int ii;
        for (ii = 0; ii < ww.length(); ++ii) {
            char cc = ww.charAt(ii);
            if (cc != '.' && !Character.isDigit(cc)) {
                break;
            }
        }
        double dd = new Double(ww.substring(0, ii)).doubleValue();
        if (ii < ww.length()) {
            ww = ww.substring(ii);
            if (ww.equals("#")) {
                dd /= 2.2047;
            } else if (ww.equals("lb")) {
                dd /= 2.2047;
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            if (!metric) {
                dd /= 2.2047;
            }
        }

        return dd;
    }

    private int parseNote(String ww) {
        int accidental = 0;
        if (ww.charAt(0) == '_') {
            accidental = -1;
            ww = ww.substring(1);
        } else if (ww.charAt(0) == '^') {
            accidental = 1;
            ww = ww.substring(1);
        }
        int note = "C D EF G A Bc d ef g a b".indexOf(ww.charAt(0));
        if (note == -1) {
            throw new IllegalArgumentException();
        }
        ww = ww.substring(1);
        if (ww.length() > 0) {
            if (Character.isDigit(ww.charAt(0))) {
                if (note >= 12) {
                    note -= 12;
                }
                note += 12 * (Character.digit(ww.charAt(0), 10) - 4);
                ww = ww.substring(1);
            } else {
                while (ww.length() > 0 && ww.charAt(0) == ',') {
                    note -= 12;
                    ww = ww.substring(1);
                }
                while (ww.length() > 0 && ww.charAt(0) == '\'') {
                    note += 12;
                    ww = ww.substring(1);
                }
            }
        }
        if (ww.length() > 0) {
            if (ww.charAt(0) == 'b') {
                accidental = -1;
                ww = ww.substring(1);
            } else if (ww.charAt(0) == '#') {
                accidental = 1;
                ww = ww.substring(1);
            }
        }
        if (ww.length() > 0) {
            throw new IllegalArgumentException();
        }
        note += accidental;
        note -= 9; // A440 is base
        return note;
    }

    private static double parseLength(StringTokenizer words, boolean metric) {
        String ww = words.nextToken();

        int ii;
        for (ii = 0; ii < ww.length(); ++ii) {
            char cc = ww.charAt(ii);
            if (cc != '.' && !Character.isDigit(cc)) {
                break;
            }
        }
        double dd = new Double(ww.substring(0, ii)).doubleValue();
        if (ii < ww.length()) {
            ww = ww.substring(ii);
        } else if (words.hasMoreTokens()) {
            ww = words.nextToken();
        } else {
            if (!metric) {
                dd *= 2.54;
            }
            return dd;
        }
        if (ww.equals("\"")) {
            dd *= 2.54;
        } else if (ww.equals("cm")) {
        } else if (ww.equals("mm")) {
            dd /= 10.;
        } else {
            throw new IllegalArgumentException();
        }

        return dd;
    }
    // Circle K String weight datas
    double[][] CKPLB = {
        {.008, .000014240},
        {.009, .000018022},
        {.010, .000022252},
        {.011, .000026925},
        {.012, .000032039},
        {.013, .000037605},
        {.014, .000043607},
        {.015, .000050050},
        {.016, .000056961},
        {.017, .000064300},
        {.018, .000072088},
        {.019, .000080360},
        {.020, .000089031},
        {.021, .000098155},
        {.022, .000107666},
        {.023, .000117702}
    };
        double[][] CKPLG = {
        {.008, .000014240},
        {.009, .000018022},
        {.010, .000022252},
        {.011, .000026925},
        {.012, .000032039},
        {.013, .000037605},
        {.014, .000043607},
        {.015, .000050050},
        {.016, .000056961},
        {.017, .000064300},
        {.018, .000072088},
        {.019, .000080360},
        {.020, .000089031},
        {.021, .000098155},
        {.022, .000107666}
    };
    double[][] CKWNG = {
        {.021, .000093873},
        {.022, .000103500},
        {.023, .000113985},
        {.024, .000124963},
        {.025, .000136054},
        {.026, .000144691},
        {.027, .000153146},
        {.028, .000161203},
        {.029, .000178551},
        {.031, .000198902},
        {.033, .000223217},
        {.035, .000249034},
        {.037, .000276237},
        {.039, .000304788},
        {.041, .000334965},
        {.043, .000366357},
        {.045, .000404956},
        {.047, .000447408},
        {.049, .000475438},
        {.051, .000512645},
        {.053, .000551898},
        {.055, .000584407},
        {.057, .000625704},
        {.059, .000679149},
        {.061, .000720293},
        {.063, .000765973},
        {.065, .000821116},
        {.067, .000870707},
        {.070, .000939851},
        {.073, .001021518},
        {.076, .001110192},
        {.079, .001188974},
        {.082, .001293598},
        {.086, .001416131},
        {.090, .001544107},
        {.094, .001677765},
        {.098, .001831487},
        {.102, .001986524},
        {.106, .002127413},
        {.112, .002367064},
        {.118, .002616406},
        {.124, .002880915},
        {.130, .003154996},
        {.136, .003441822},
        {.142, .003741715},
        {.150, .004051506},
        {.158, .004375389},
        {.166, .005078724},
        {.174, .005469937},
        {.182, .006071822},
        {.190, .006605072},
        {.200, .007311717},
        {.210, .008037439},
        {.222, .009091287},
        {.232, .009888443},
        {.244, .010907182},
        {.254, .011787319}
    };
        double[][] CKWNB = {
        {.025, .000124568},
        {.026, .000144691},
        {.027, .000153146},
        {.028, .000161203},
        {.029, .000178551},
        {.031, .000198902},
        {.033, .000223217},
        {.035, .000249034},
        {.037, .000276237},
        {.039, .000304788},
        {.041, .000334965},
        {.043, .000366357},
        {.045, .000404956},
        {.047, .000447408},
        {.049, .000475438},
        {.051, .000512645},
        {.053, .000551898},
        {.055, .000584407},
        {.057, .000625704},
        {.059, .000679149},
        {.061, .000720293},
        {.063, .000765973},
        {.065, .000821116},
        {.067, .000870707},
        {.070, .000939851},
        {.073, .001021518},
        {.076, .001110192},
        {.079, .001188974},
        {.082, .001293598},
        {.086, .001416131},
        {.090, .001544107},
        {.094, .001677765},
        {.098, .001831487},
        {.102, .001986524},
        {.106, .002127413},
        {.112, .002367064},
        {.118, .002616406},
        {.124, .002880915},
        {.130, .003154996},
        {.136, .003441822},
        {.142, .003741715},
        {.150, .004051506},
        {.158, .004375389},
        {.166, .005078724},
        {.174, .005469937},
        {.182, .006071822},
        {.190, .006605072},
        {.200, .007311717},
        {.210, .008037439},
        {.222, .009091287},
        {.232, .009888443},
        {.244, .010907182},
        {.254, .011787319}
    };
    // D'addario string weight datas
    double[][] DAPL = {
        {.007, .00001085},
        {.008, .00001418},
        {.0085, .00001601},
        {.009, .00001794},
        {.0095, .00001999},
        {.010, .00002215},
        {.0105, .00002442},
        {.011, .00002680},
        {.0115, .00002930},
        {.012, .00003190},
        {.013, .00003744},
        {.0135, .00004037},
        {.014, .00004342},
        {.015, .00004984},
        {.016, .00005671},
        {.017, .00006402},
        {.018, .00007177},
        {.019, .00007997},
        {.020, .00008861},
        {.022, .00010722},
        {.024, .00012760},
        {.026, .00014975}
    };
    double[][] DAPB = {
        {.020, .00008106},
        {.021, .00008944},
        {.022, .00009876},
        {.023, .00010801},
        {.024, .00011682},
        {.025, .00012686},
        {.026, .00013640},
        {.027, .00014834},
        {.029, .00017381},
        {.030, .00018660},
        {.032, .00021018},
        {.034, .00023887},
        {.035, .00025365},
        {.036, .00026824},
        {.039, .00031124},
        {.042, .00036722},
        {.045, .00041751},
        {.047, .00045289},
        {.049, .00049151},
        {.052, .00055223},
        {.053, .00056962},
        {.056, .00063477},
        {.059, .00070535},
        //Added in 0.1.9
        {.060, .00073039},
        {.062, .00077682},
        {.064, .00082780},
        {.066, .00087718},
        {.070, .00096833}
    };
    double[][] DAXSG = {
        {.018, .00006153},
        {.020, .00007396},
        {.021, .00008195},
        {.022, .00009089},
        {.024, .00010742},
        {.026, .00012533},
        {.028, .00014471},
        {.030, .00017002},
        {.032, .00019052},
        {.034, .00021229},
        {.036, .00023535},
        {.038, .00025969},
        {.040, .00028995},
        {.042, .00031685},
        {.046, .00037449},
        {.048, .00040523},
        {.050, .00043726},
        {.052, .00047056},
        {.054, .00052667},
        {.056, .00056317},
        {.070, .00087444}
    };
    double[][] DANW = {
        {.017, .00005524},
        {.018, .00006215},
        {.019, .00006947},
        {.020, .00007495},
        {.021, .00008293},
        {.022, .00009184},
        {.024, .00010857},
        {.026, .00012671},
        {.028, .00014666},
        {.030, .00017236},
        {.032, .00019347},
        {.034, .00021590},
        {.036, .00023964},
        {.038, .00026471},
        {.039, .00027932},
        {.042, .00032279},
        {.044, .00035182},
        {.046, .00038216},
        {.048, .00041382},
        {.049, .00043014},
        {.052, .00048109},
        {.056, .00057598},
        {.059, .00064191},
        {.060, .00066542},
        {.062, .00070697},
        {.064, .00074984},
        {.066, .00079889},
        {.068, .00084614},
        {.070, .00089304},
        {.072, .00094124},
        {.074, .00098869},
        {.080, .00115011}
    };
    double[][] DAHRG = {
        {.022, .00011271},
        {.024, .00013139},
        {.026, .00015224},
        {.030, .00019916},
        {.032, .00022329},
        {.036, .00027556},
        {.039, .00032045},
        {.042, .00036404},
        {.046, .00043534},
        {.052, .00054432},
        {.056, .00062758}
    };
    //Added in 0.1.9 - Chromes - Stainless steel Flat wound
    double[][] DACG = {
        {.020, .00007812},
        {.022, .00009784},
        {.024, .00011601},
        {.026, .00013574},
        {.028, .00014683},
        {.030, .00016958},
        {.032, .00019233},
        {.035, .00024197},
        {.038, .00026520},
        {.040, .00031676},
        {.042, .00034377},
        {.045, .00040393},
        {.048, .00043541},
        {.050, .00047042},
        {.052, .00049667},
        {.056, .00059075},
        {.065, .00089364}
    };
    // Added in 0.1.9 - Flat Tops - Phosphore Bronze Polished
    double[][] DAFT = {
        {.023, .00012568},
        {.024, .00013651},
        {.026, .00015894},
        {.028, .00017209},
        {.030, .00019785},
        {.032, .00023852},
        {.035, .00027781},
        {.036, .00029273},
        {.039, .00032904},
        {.042, .00036219},
        {.044, .00041047},
        {.045, .00042603},
        {.047, .00046166},
        {.053, .00055793},
        {.056, .00064108}
    };
    //added in v0.1.9 - 80-20’S- 80/20 Brass Round Wound
    double[][] DABW = {
        {.020, .00007862},
        {.021, .00008684},
        {.022, .00009600},
        {.023, .00010509},
        {.024, .00011353},
        {.025, .00012339},
        {.026, .00013253},
        {.027, .00014397},
        {.029, .00016838},
        {.030, .00018092},
        {.032, .00020352},
        {.034, .00022752},
        {.035, .00024006},
        {.036, .00025417},
        {.039, .00030063},
        {.042, .00034808},
        {.045, .00040245},
        {.047, .00043634},
        {.049, .00047368},
        {.052, .00053224},
        {.053, .00054852},
        {.056, .00061132},
        {.059, .00068005}
    };
    //added in v0.1.9 - Great American Bronze - 85/15 Brass Round Wound
    double[][] DAZW = {
        {.022, .00009802},
        {.024, .00011594},
        {.025, .00012592},
        {.026, .00013536},
        {.030, .00018507},
        {.032, .00020839},
        {.034, .00023316},
        {.035, .00024610},
        {.036, .00026045},
        {.040, .00032631},
        {.042, .00035735},
        {.044, .00038985},
        {.045, .00040665},
        {.046, .00042565},
        {.050, .00050824},
        {.052, .00054686},
        {.054, .00058694},
        {.056, .00062847}
    };
    //added in v0.1.9 - Bass - Nickplated Round Wound
    double[][] DAXB = {
        {.018, .00007265},
        {.020, .00009093},
        {.028, .00015433},
        {.042, .00032252},
        {.052, .00051322},
        {.032, .00019000},
        {.035, .00022362},
        {.040, .00029322},
        {.045, .00037240},
        {.050, .00046463},
        {.055, .00054816},
        {.060, .00066540},
        {.065, .00079569},
        {.070, .00093218},
        {.075, .00104973},
        {.080, .00116023},
        {.085, .00133702},
        {.090, .00150277},
        {.095, .00169349},
        {.100, .00179687},
        {.105, .00198395},
        {.110, .00227440},
        {.120, .00250280},
        {.125, .00274810},
        {.130, .00301941},
        {.135, .00315944},
        {.145, .00363204}
    };
    //added in v0.1.9 - Bass - Half Round - Pure Nickel Half Round
    double[][] DAHB = {
        {.030, .00019977},
        {.040, .00031672},
        {.045, .00039328},
        {.050, .00046898},
        {.055, .00058122},
        {.060, .00070573},
        {.065, .00080500},
        {.070, .00096476},
        {.075, .00103455},
        {.080, .00118785},
        {.085, .00138122},
        {.090, .00140885},
        {.095, .00166888},
        {.100, .00185103},
        {.105, .00205287},
        {.110, .00220548},
        {.130, .00301941}
    };
    //added in v0.1.9 - Bass - Chromes - Stainless Steel Flat Wound
    double[][] DABC = {
        {.040, .00032716},
        {.045, .00039763},
        {.050, .00047855},
        {.055, .00056769},
        {.060, .00070108},
        {.065, .00080655},
        {.070, .00093374},
        {.075, .00100553},
        {.080, .00120719},
        {.085, .00138675},
        {.090, .00148896},
        {.095, .00173288},
        {.100, .00189041},
        {.105, .00204302},
        {.110, .00226455},
        {.132, .00314193}
    };
    //added in v0.1.9 - Bass - ProSteels - ProSteel Round Wound
    double[][] DABS = {
        {.032, .00020465},
        {.040, .00029409},
        {.045, .00036457},
        {.050, .00042635},
        {.055, .00058296},
        {.060, .00067781},
        {.065, .00073365},
        {.070, .00087014},
        {.075, .00095857},
        {.080, .00111879},
        {.085, .00124034},
        {.090, .00151382},
        {.095, .00156550},
        {.100, .00169349},
        {.105, .00183626},
        {.110, .00218579},
        {.125, .00263432},
        {.130, .00277435},
        {.145, .00327321}
    };
}
