package my.javasaml;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import com.onelogin.saml2.util.Util;

public class KeyConstants {
	public static final String publicKeyText = "MIICRjCCAa+gAwIBAgIBADANBgkqhkiG9w0BAQ0FADBAMQswCQYDVQQGEwJjYTELMAkGA1UECAwCYmMxDzANBgNVBAoMBmJvbmJvbjETMBEGA1UEAwwKYm9uYm9tLmNvbTAeFw0yMDA4MjAwMDM1MzVaFw0yMTA4MjAwMDM1MzVaMEAxCzAJBgNVBAYTAmNhMQswCQYDVQQIDAJiYzEPMA0GA1UECgwGYm9uYm9uMRMwEQYDVQQDDApib25ib20uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGi5pBQVB3hShfO6kdL2FFHoMCBGfgxEEiGrh+998LSlrpaEpAQLZTQ/E2JTQBQ5zCv5s+xztQKLMLfk1RbdJ+Jr+5+dMK8CEw9HO0MdntwBEl41IofUEHDyvWtLpsmYUpfVlQ8hH1zFfyza/e/FFuBCCgBM74jO2yXmV+UvGdiwIDAQABo1AwTjAdBgNVHQ4EFgQUmPw/1yl8PUrjZ+f+bbeeD/wupxcwHwYDVR0jBBgwFoAUmPw/1yl8PUrjZ+f+bbeeD/wupxcwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQ0FAAOBgQBPnJCDQJtZCA0N3T1p1hqRK79r1vFfxzm0oMXQKQIlLMKc8X3L3bjLKnQTSc1BmZIhL7muF2A9gityYyyEfPoVUHe9jmHYtje5Txk+ZjqTTbdQhC/3BbeyHUV6OuTbp9NCVjcnTKIPoKwlGTz6R2diysVTjrBEgFCAxCT/j9E7OQ==";
	public static final String fingerprint = "9b605c649708accb654e626c0dc6c78714c09225";
	public static final String privateKeyText = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMaLmkFBUHeFKF87qR0vYUUegwIEZ+DEQSIauH733wtKWuloSkBAtlND8TYlNAFDnMK/mz7HO1Aoswt+TVFt0n4mv7n50wrwITD0c7Qx2e3AESXjUih9QQcPK9a0umyZhSl9WVDyEfXMV/LNr978UW4EIKAEzviM7bJeZX5S8Z2LAgMBAAECgYAqSG7IeOVvHr+TUHxah82aT74svs10k7cfUTt7ZsRsfiBQVvKBLoblwrFrC49Auap32yBvxTQ/hPtkdjh3IAFUllmz9ios8RlgsxBYbQ3QfivEWg7EuB6+7XVYtsiWu/joxoQmfxIE6Pjdd0NnSmgSxbyep2e3tbeUsMptTugaOQJBAOPpmevZn+upT20g/q/nWPpJNBCOIAw2+6TPCtmQ33XTQ4ln6swJnN38RGeO7kF1go0TjxdY/kxgLDSPtR7LPlUCQQDfA39q1mthFvvN/oKQfQkGVxzy3LUGIhvF2Py3iVekyvVHb/V81gkWms56EaIHFPqHAF3DEMhHyuhODvmCs4xfAkBnXl/tVHrvy462lCxvY6I7glAW4h8u6xEfIrhtDQQM0JDlFTis6f83v17XCUCOd9mKWsCfekF8KnJiNHuVR+ExAkEAqUymdhzA97vS/Naxl67UIxQOze/vHPDX1KKdd1e0SU4qVkkQO+zgiWCMTu8D6gxrBAGrLGKZKKdJI7kKgcZ8UQJBANoBQfqz4I/CVUJA6Cz1G7xPnQkW2BBNIr70s0FoU0LMdHZqyj0L7ZFm7WJo4qsUMVlUCtC10XHhySXO6IEvg4s=";
	
	public static PublicKey publicKey;
	public static X509Certificate cert;
	public static PrivateKey privateKey;
	
	static {
		try {
			cert = Util.loadCert(publicKeyText);
			publicKey = cert.getPublicKey();
			privateKey = Util.loadPrivateKey(privateKeyText);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}