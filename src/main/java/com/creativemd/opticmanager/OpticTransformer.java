package com.creativemd.opticmanager;

import static org.objectweb.asm.Opcodes.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.creativemd.creativecore.transformer.CreativeTransformer;
import com.creativemd.creativecore.transformer.Transformer;
import com.creativemd.creativecore.transformer.TransformerNames;

public class OpticTransformer extends CreativeTransformer{
	
	public OpticTransformer() {
		super("opticmanager");
	}

	@Override
	protected void initTransformers() {
		addTransformer(new Transformer("net.minecraft.world.World") {
			
			@Override
			public void transform(ClassNode node) {
				MethodNode m = findMethod(node, "getSunBrightnessFactor", "(F)F");
				m.instructions.clear();
				m.instructions.add(new VarInsnNode(ALOAD, 0));
				m.instructions.add(new VarInsnNode(FLOAD, 1));
				m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/creativemd/opticmanager/OpticWorldUtils", "getSunBrightness", patchDESC("(Lnet/minecraft/world/World;F)F"), false));
				m.instructions.add(new InsnNode(FRETURN));
				
				m = findMethod(node, "getSunBrightnessBody", "(F)F");
				if(m != null)
				{
					m.instructions.clear();
					m.instructions.add(new VarInsnNode(ALOAD, 0));
					m.instructions.add(new VarInsnNode(FLOAD, 1));
					m.instructions.add(new MethodInsnNode(INVOKESTATIC, "com/creativemd/opticmanager/OpticWorldUtils", "getSunBrightnessClient", patchDESC("(Lnet/minecraft/world/World;F)F"), false));
					m.instructions.add(new InsnNode(FRETURN));
				}
			}
		});
	}
}
