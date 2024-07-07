# Minecraft Vanilla Notebook

This is a small, vanilla-styled mod that lets you write notes in-game.

It uses a similar GUI as a traditional book, but it's completely client-side and global - It will persist across worlds and servers.

### Important notice for people updating the mod

I try and avoid breaking changes, alas, the way I stored books previously was bad and changing it requires some user intervention.

Versions older than v4.0.0 will be incompatible with newer versions, your notebooks will be reset otherwise.

Updating them is simple, head into `.minecraft/Notebook` and you will see folders, each containing files with the contents of each page (Who thought this was a good way to store them.... oh wait, that was me. oh the regrets.)

Now, create a file `[bookname].json` for each book, replacing `[bookname]` with whatever you want the book to be called.
In the file, copy the below, replacing `default.json` with whatever you named the file;

```json
{
  "content" : ["page 1 content","page 2 content"],
  "location" : "default.json"
}
```

Now, in `content`, you can add comma separated values, surrounded by `""`, containing the text for each page, replacing `"page 1 content"` and `"page 2 content"`.

After that, you can reload the game and they should appear. 

## FAQ

> What versions?

I try and keep it on the latest version, but if people stick to specific versions I will support those for longer, until something else becomes more relevant. Versions older than 1.20.5 may not be supported for very long, as some changes in Java 21 are very useful.

> Forge/Fabric?

The mod should work on fabric and quilt already. However, forge is probably not going to get an official port.

If you want to port it to forge yourself, you're more than welcome to - I will accept PRs and add branches to support forge if someone wants to maintain them.

> Can I use this in my modpack?

Yes, you can use it in your modpack. Preferably, with credit.

> Where do I download it?

It's downloadable on GitHub, my Forgejo instance Modrinth and Curseforge. 

Curseforge is the least priority, so I would recommend checking here or Modrinth first.

Go to the releases tab to see it here, or go to a different site;

- Modrinth: https://modrinth.com/mod/notebook
- Curseforge: https://www.curseforge.com/minecraft/mc-mods/notebook
- Forgejo: https://git.sillyjune.xyz/juniper/Minecraft-Vanilla-Notebook
- Github: https://github.com/JunePrimavera/Minecraft-Vanilla-Notebook

> I found a bug! / I want to suggest features!

If you find bugs or issues with the mod, you can make an issue [on GitHub](https://github.com/JunePrimavera/Minecraft-Vanilla-Notebook) or [my forgejo instance](https://git.sillyjune.xyz/juniper/Minecraft-Vanilla-Notebook/issues).

Alternatively, you can [join my discord](https://discord.gg/EqTwbVYEWx) if you have any questions, need help, or want to talk to me directly.

If you want to help support this mod, you can do so by translating it into other languages, improving textures. If you want to add textures/translations (and don't want/know how to use GitHub's PRs), contact me on discord.

Please do not DM me for feature requests - If you are asking about contributing or bugs, feel free to.

