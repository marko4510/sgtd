package com.example.pasarela.Models.Utils;

import java.io.FileOutputStream;
import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





import java.io.FileInputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PrivateKeySignature;

public class Archive {
    private final Logger log = LoggerFactory.getLogger(getClass());

	public void sign(String keystore, char[] password,int level,  String src, String dest)
			throws GeneralSecurityException, IOException, DocumentException {

	

		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(keystore), password);
		String alias = (String) ks.aliases().nextElement();
		PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
		Certificate[] chain = ks.getCertificateChain(alias);
		// Creating the reader and the stamper
		PdfReader reader = new PdfReader(src);
		FileOutputStream os = new FileOutputStream(dest);
		PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0', null, true);
		// Creating the appearance
		PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
		// appearance.setVisibleSignature(name);
		appearance.setReason("USIC Unidad de Sistemas de Informacion y Comunicacion");
		appearance.setLocation("Cobija - Pando");
        appearance.setCertificationLevel(level);
		// Creating the signature
		ExternalSignature pks = new PrivateKeySignature(pk, "SHA-256", "BC");
		ExternalDigest digest = new BouncyCastleDigest();
		MakeSignature.signDetached(appearance, digest, pks, chain, null, null, null, 0, CryptoStandard.CMS);

		os.close();
		stamper.close();
		reader.close();
	}

 

}
