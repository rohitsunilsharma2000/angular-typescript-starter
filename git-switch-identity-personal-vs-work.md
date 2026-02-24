 
## Option 1) Best practice: use **repo-local** identity (switch per project)

### Set Dufry identity for this repo

```bash
git config user.name "Meghnad Saha"
git config user.email "meghnad.saha@dufry.com"
```

### Switch back to old identity for this repo

```bash
git config user.name "rohitsunilsharma2000"
git config user.email "rohitsunilsharma2000@gmail.com"
```

### Check what this repo will use

```bash
git config user.name
git config user.email
```

✅ This affects only the current repo.

---

## Option 2) Keep **global = old**, and only set Dufry email in work repos

This is what most people do.

### Set your personal identity globally (default)

```bash
git config --global user.name "rohitsunilsharma2000"
git config --global user.email "rohitsunilsharma2000@gmail.com"
```

### In a Dufry repo, override locally

```bash
git config user.name "Meghnad Saha"
git config user.email "meghnad.saha@dufry.com"
```

### See which one Git will actually use (shows where it comes from)

```bash
git config --show-origin --get user.name
git config --show-origin --get user.email
```

---

## Option 3) One-time commit with different author (without changing config)

If you want **just one commit** to be from Dufry email:

```bash
git commit --author="Meghnad Saha <meghnad.saha@dufry.com>" -m "your message"
```

Or set identity only for that single command:

```bash
git -c user.name="Meghnad Saha" -c user.email="meghnad.saha@dufry.com" commit -m "your message"
```

---

## Recommended setup (simple)

* Global = your personal (old) email
* Local config in each work repo = Dufry email
 
