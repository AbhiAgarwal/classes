#### Breaking cryptosystems (p10)

1. Cryptanalysis
2. Implementation attacks
	- Exploiting software bugs
	- Side channel attack
		- Timing analysis attacks: time the encryption/decryption methods to figure out the secret key
		- Power analysis attacks
3. Social Engineering attacks
	- Phishing

#### Modular Arithmetic

Congruent. Congruences
Equivalence relation
	- Reflexive
	- Symmetric
	- Transitive

a ≡ r (mod n)
a = r + nq
r = a - nq

---

a ≡ b (mod n)

Fix n. It's a binary relationship

a ≡ b + nq_1
c ≡ b + nq_2

a - c = n(q_1 - q_2)

---

Residue class
Equivalance class

---

Compatibility

if
	a ≡ a' (mod n)
	b ≡ b' (mod n)

then
	a + b ≡ a' + b' (mod n)
	a * b ≡ a' * b' (mod n)

proof
	a = a' + nq_1
	b = b' + nq_2

	a*b = a'*b' + nQ (all other a*nq_2, etc. contain n - we summarize it into Q)

---

Problems can be broken down

(abstract algebra)

---

Ring

