package com.creativemd.opticmanager;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.launchwrapper.IClassTransformer;

public class OpticManagerTransformer implements IClassTransformer {
	
	public static boolean obfuscated = false;
	
	public static String[] names = new String[]{".", "net/minecraft/client/audio/SoundManager$SoundSystemStarterThread",
		"net/minecraft/client/audio/SoundHandler","net/minecraft/client/audio/SoundManager","sndManager", "sndSystem"};
	public static String[] namesO = new String[]{"/", "bta", "btc", "bsw", "f", "e"};
	
	public static String[] namesP = new String[]{"/", "bta", "btc", "bsw", "field_147694_f", "field_148620_e"};
	
	public static String patchP(String input)
	{
		String before = input;
		if(obfuscated)
		{
			for(int zahl = 0; zahl < names.length; zahl++)
				input = input.replace(names[zahl], namesP[zahl]);
		}
		return input;
	}
	
	public static String patch(String input)
	{
		String before = input;
		if(obfuscated)
		{
			for(int zahl = 0; zahl < names.length; zahl++)
				input = input.replace(names[zahl], namesO[zahl]);
		}
		return input;
	}
	
	@Override
	public byte[] transform(String name, String transformedName,
			byte[] basicClass) {
		/*if(name.equals("btc") | name.equals("bsw"))
			obfuscated = true;
		if(name.equals("btc") | name.equals("net.minecraft.client.audio.SoundHandler"))
		{
			basicClass = modifyClass(name, basicClass, VoiceChatLoader.location);
			System.out.println("[VoiceChat] Patching " + name);
		}
		if(name.equals("bsw") | name.equals("net.minecraft.client.audio.SoundManager"))
		{
			basicClass = modifyClass2(name, basicClass, VoiceChatLoader.location);
			System.out.println("[VoiceChat] Patching " + name);
		}*/
		return basicClass;
	}
	
	public byte[] modifyClass(String name, byte[] bytes, File location)
	{
		String fieldname = patch("sndManager");
		String fielddesc = patch("Lnet/minecraft/client/audio/SoundManager;");
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		Iterator<FieldNode> fields = classNode.fields.iterator();
		while(fields.hasNext())
		{
			FieldNode f = fields.next();
			if (f.name.equals(fieldname) && f.desc.equals(fielddesc))
			{
				f.access = ACC_PUBLIC + ACC_FINAL;
				System.out.println("Patching " + fieldname + " ...");
				break;
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}
	
	public byte[] modifyClass2(String name, byte[] bytes, File location)
	{
		String fieldname = patch("sndSystem");
		String fielddesc = patch("Lnet/minecraft/client/audio/SoundManager$SoundSystemStarterThread;");
		
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);
		
		Iterator<FieldNode> fields = classNode.fields.iterator();
		while(fields.hasNext())
		{
			FieldNode f = fields.next();
			if (f.name.equals(fieldname) && f.desc.equals(fielddesc))
			{
				f.access = ACC_PUBLIC;
				System.out.println("Patching " + fieldname + " ...");
				break;
			}
		}
		
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		return writer.toByteArray();
	}

}
