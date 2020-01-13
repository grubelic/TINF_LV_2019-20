package hr.fer.tinf.lab.zad3;

import java.util.*;

public class BinaryBlockCode {
	
	private final Map<BinaryVector, String> codewords = new HashMap<>();
	private final Set<BinaryVector> vectorSpace = new HashSet<>();

	private final int n;
	private boolean safeDecoding = true;

	public boolean toggleSafety() {
		return safeDecoding = !safeDecoding;
	}

	public BinaryBlockCode(int n) {
		if (n < 1) {
			throw new IllegalArgumentException(
				"Block size has to be a natural number smaller than 10^6."
			);
		} else if (n > 1000000) {
			throw new IllegalArgumentException(
				"Block size is too large."
			);
		}
		vectorSpace.add(new BinaryVector(n));
		this.n = n;
	}

	public int getN() {
		return n;
	}

	public int getK() {
		if (codewords.size() == 0) return 0;
		return (int) Math.ceil(Math.log(codewords.size()) / Math.log(2));
	}

	public boolean isLinear() {
		return vectorSpace.size() == codewords.size();
	}

	/**
	 * Creates standard array by appending vectors of minimal weight as long as
	 * they are not present in standard array
	 * @return standard array of the block code
	 */
	public BinaryVector[][] getStandardArray() {
		List<BinaryVector[]> sArray = new ArrayList<>();
		sArray.add(new BinaryVector[codewords.size()]);
		List<Set<BinaryVector>> seen = new ArrayList<>(n);
		for (int i = 0; i <= n; i++) {
			seen.add(new HashSet<>());
		}
		int i = 1;
		for (BinaryVector bv : codewords.keySet()) {
			seen.get(bv.weight()).add(bv);
			if (!bv.equals(new BinaryVector(n))) {
				sArray.get(0)[i++] = bv;
			} else {
				sArray.get(0)[0] = bv;
			}
		}
		int weight = 0;
		Iterator<BinaryVector> it = seen.get(weight).iterator();
		BinaryVector[] row = new BinaryVector[codewords.size()];
		int bound = safeDecoding ? getT() : n;
		for (; weight < bound; weight += it.hasNext() ? 0 : 1) {
			if (!it.hasNext()) {
				it = seen.get(weight).iterator();
				continue;
			}
			BinaryVector bv = it.next();
			BinaryVector temp = bv.times(true);
			for (i = 0; i < temp.dim(); i++) {
				if (!temp.get(i)) {
					temp.set(i);
					boolean anySeen = false;
					for (int j = 0; j < row.length; j++) {
						row[j] = temp.plus(sArray.get(0)[j]);
						anySeen |= seen.get(row[j].weight()).contains(row[j]);
					}
					if (!anySeen) {
						sArray.add(row);
						for (BinaryVector v : row) {
							seen.get(v.weight()).add(v);
						}
						row = new BinaryVector[codewords.size()];
					}
					temp.set(i, false);
				}
			}
		}
		BinaryVector[][] result =
			new BinaryVector[sArray.size()][codewords.size()];
		for (int j = 0; j < result.length; j++) {
			System.arraycopy(sArray.get(j), 0, result[j], 0, result[0].length);
		}
		return result;
	}

	public int distance() {
		int minDist = Integer.MAX_VALUE;
		for (BinaryVector bv1 : codewords.keySet()) {
			for (BinaryVector bv2 : codewords.keySet()) {
				if (!bv1.equals(bv2)) {
					minDist = Math.min(BinaryVector.dist(bv1, bv2), minDist);
				}
			}
		}
		return minDist;
	}

	public int getT() {
		return (distance() - 1) >> 1;
	}

	public double correctDecodingProbability(double p) {
		if (p < 0 || p > 1) {
			throw new IllegalArgumentException(
				"Probability has to be in range[0, 1]"
			);
		}
		BinaryVector[][] sa = getStandardArray();
		double result = 0;
		for (BinaryVector[] binaryVectors : sa) {
			double weight = binaryVectors[0].weight();
			result += Math.pow(p, weight) * Math.pow(1 - p, n - weight);
		}
		return result;
	}

	public String decode(BinaryVector codeword) {
		if (codeword.dim() != n) {
			throw new IllegalArgumentException(
				"Codeword doesn't match this block code's length n (" + n + ")"
			);
		}
		StringBuilder sb = new StringBuilder();
		if (codewords.containsKey(codeword)) {
			sb.append(
				"Codeword is correctly entered or error can't be detected\n"
			);
			if (codewords.get(codeword) != null) {
				sb.append(codeword.toString());
				sb.append(" -> ").append(codewords.get(codeword));
			}
		} else {
			sb.append("Error detected.\n");
			BinaryVector[][] sa = getStandardArray();
			for (BinaryVector[] coset : sa) {
				BinaryVector cosetLeader = coset[0];
				for (BinaryVector cosetMember : coset) {
					if (cosetMember.equals(codeword)) {
						sb.append("Codeword found in standard array.\n");
						sb.append("Error vector: ");
						sb.append(cosetLeader.toString());
						sb.append("\nCorrect codeword (input - error): ");
						BinaryVector correct = codeword.minus(cosetLeader);
						sb.append(correct);
						if (codewords.get(correct) != null) {
							sb.append(" -> ").append(codewords.get(correct));
						}
						int w = cosetLeader.weight();
						if (w > getT()) {
							sb.append("\nNote: error vector weight is larger");
							sb.append(" than number of decodable errors.\n");
							sb.append("Other codewords with same distance: ");
							for (BinaryVector bv : codewords.keySet()) {
								if (BinaryVector.dist(bv, cosetMember) == w) {
									sb.append('\n').append(bv.toString());
								}
							}
						}
						return sb.toString();
					}
				}
			}
			sb.append("Codeword not present in standard array. Can't correct.");
		}
		return sb.toString();
	}

	public void removeCodeword(BinaryVector v) {
		List<Map.Entry<BinaryVector, String>> temp
			= new ArrayList<>(codewords.entrySet());
		codewords.clear();
		vectorSpace.clear();
		vectorSpace.add(new BinaryVector(n));
		for (Map.Entry<BinaryVector, String> e : temp) {
			if (!e.getKey().equals(v)) {
				addCodeWord(e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * Adds a vector and if it's not collinear with others, extends vector space
	 * @param newCodeword Codeword
	 * @param newSymbol Symbol
	 */
	public void addCodeWord(BinaryVector newCodeword, String newSymbol) {
        if (newCodeword.dim() != n) {
            throw new IllegalArgumentException(
                "Codeword doesn't match this block code's length n (" + n + ")"
            );
        }
        
        if ("".equals(newSymbol)) newSymbol = null;
        
        String oldSymbol = codewords.get(newCodeword);
        if (oldSymbol != null) {
        	if (!oldSymbol.equals(newSymbol)) {
        		throw new IllegalArgumentException(
                    "Codeword " + newCodeword + " is already associated with "
            		+ oldSymbol
                );
        	}
        }  else {
        	if (!vectorSpace.contains(newCodeword)) {
				Set<BinaryVector> temp = new HashSet<>();
        		for (BinaryVector bv : vectorSpace) {
        			temp.add(bv.plus(newCodeword));
				}
        		vectorSpace.addAll(temp);
			}
        	codewords.put(newCodeword, newSymbol);
        }
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<BinaryVector, String> it : codewords.entrySet()) {
			sb.append(it.getKey().toString());
			if (it.getValue() != null) {
				sb.append("->");
				sb.append(it.getValue());
			}
			sb.append('\n');
		}
		sb.append("n = ").append(n).append(", k = ").append(getK());
		return sb.toString();
	}
}
