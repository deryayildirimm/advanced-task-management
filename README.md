# 📝 Advanced Task Management

Bu proje, **Spring Boot 3 & PostgreSQL** kullanılarak geliştirilmiş bir **görev yönetim** uygulamasıdır.\
Docker kullanarak PostgreSQL veritabanını hızlıca çalıştırabilir ve uygulamayı kolayca başlatabilirsiniz.

---

## 🚀 Projeyi Çalıştırmak İçin

Aşağıdaki adımları takip ederek projeyi local ortamında çalıştırabilirsin:

### 1️⃣ Projeyi Klonla

```sh
git clone https://github.com/deryayildirimm/advanced-task-management.git
cd advanced-task-management
```

---

### 2️⃣ **.env Dosyanı Hazırla**
Projede ortam değişkenlerini tanımlamak için `.env` dosyası gereklidir.

1. `.env` dosyanızı oluşturmak için aşağıdaki komutu çalıştırın:
```sh
cp env.example .env
```

2. `.env` dosyanızı açarak aşağıdaki bilgileri ekleyin:
```ini
# PostgreSQL Veritabanı Ayarları
POSTGRES_DB=veritabani_adi
POSTGRES_USER=kullanici_adi
POSTGRES_PASSWORD=sifre

# Spring Security Varsayılan Kullanıcı Bilgileri
SPRING_SECURITY_USER=admin
SPRING_SECURITY_PASSWORD=admin123
```

<details>
<summary>IntelliJ'de .env Dosyasını Kullanma</summary>

#### 📌 1️⃣ IntelliJ IDEA'ya `.env` Dosyasını Okutmak için Plugin Kullanma

Eğer IntelliJ'nin **otomatik olarak `.env` dosyasını okumasını** istiyorsan:

1. **IntelliJ IDEA'da** `"File"` → `"Settings"` → `"Plugins"` menüsüne gir.
2. **Marketplace'te `"EnvFile"` eklentisini ara** ve yükle.
3. **"Run/Debug Configurations"** menüsüne git.
4. **Sağ üstte `"EnvFile"` sekmesini aç** ve `"Enable EnvFile"` kutucuğunu işaretle.
5. **"+" butonuna basıp proje kök dizinindeki `.env` dosyanı seç.**
6. **Apply ve OK butonlarına bas.**

💡 **Bu yöntemle IntelliJ her çalıştırmada `.env` dosyasını otomatik olarak okuyacaktır.**

---

#### 📌 2️⃣ IntelliJ Üzerinden Manuel Environment Değişkenleri Tanımlama

Eğer `.env` dosyasını plugin olmadan kullanmak istiyorsan:

1. **Üst Menüden → `"Run"` → `"Edit Configurations"`** seçeneğine tıkla.
2. **Çalıştırmak istediğin Spring Boot uygulamasını seç.**
3. **"Environment Variables" kısmına tıkla.**
4. **"+" butonuna bas** ve aşağıdaki değerleri elle ekle:

| **Key (Değişken Adı)**  | **Value (Değer)** |
|-----------------|------------------|
| `POSTGRES_DB`  | `mydatabase`      |
| `POSTGRES_USER` | `myuser`         |
| `POSTGRES_PASSWORD` | `mypassword` |

5. **Apply ve OK butonlarına bas.**

💡 **Bu yöntemle environment değişkenleri sadece IntelliJ içinde bu proje için tanımlanmış olur.**

</details>



### 3️⃣ **Docker ile PostgreSQL’i Başlat**
Docker ile PostgreSQL konteynerini çalıştırmak için aşağıdaki komutu çalıştırın:
```sh
docker compose up -d
```
✅ **Bu komut, PostgreSQL veritabanını Docker konteyneri olarak başlatacaktır.**

📌 **Çalışan container’ları görmek için:**
```sh
docker ps
```
📌 **Eğer PostgreSQL çalışıyorsa, `task-postgres` isimli bir container listelenmelidir.**

---

### 4️⃣ **Veritabanı Bağlantısını Test Et**
PostgreSQL’e bağlanmayı test etmek için şu komutu çalıştırın:
```sh
docker exec -it task-manager psql -U myuser -d mydatabase
```
✅ **Eğer bağlantı başarılıysa, PostgreSQL’in çalıştığını doğrulamış olursunuz.**

---

### 5️⃣ **Spring Boot Uygulamasını Çalıştır**
Spring Boot uygulamasını başlatmak için aşağıdaki komutu çalıştırın:
```sh
mvn spring-boot:run
```
✅ **Eğer her şey doğru yapılandırıldıysa, uygulama `http://localhost:8080` adresinde çalışacaktır.**

📌 **Eğer `Spring Security` etkinse, giriş yapmanız gerekebilir.** Varsayılan kullanıcı adı ve şifre `.env` dosyasından alınacaktır.

- **Kullanıcı Adı:** `${SPRING_SECURITY_USER}`
- **Şifre:** `${SPRING_SECURITY_PASSWORD}`

Bu bilgileri `.env` dosyanızda güncelleyerek kendi giriş bilgilerinizi belirleyebilirsiniz.. Varsayılan kullanıcı adı ve şifreyi `application.properties` içinde belirleyebilirsiniz.**

