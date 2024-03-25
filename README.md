![Imagem da splash com o logo do aplicativo](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Tela%20de%20Carregamento.png)
# Tempero Digital
Olá! Este é o tempero digital, o aplicativo construído como trabalho final para o projeto de Residência em TIC12. Segue abaixo explicação e tour pelo aplicativo.

## Tela de Cardápio
![Imagem da activity de cardápio](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Tela%20de%20Card%C3%A1pio.png)
A tela do cardápio exibe os nossos pratos de entrada, principais, bebidas e sobremesas. Aqui, utilizamos um RecyclerView dentro de outro para mostrar todos os itens de forma performática na tela. Veja o código:

```kotlin
class MenuListActivity : AppCompatActivity(), MenuListRecyclerViewClickListener {
    private lateinit var menuList: MenuList
    private var cart = Cart()
    private lateinit var menuItemDetailActivityLauncher: ActivityResultLauncher<Intent>
    private lateinit var cartActivityLauncher : ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMenuListBinding

    companion object {
        const val INTENT_EXTRA_MENU_ITEM = "menuItem"
        const val INTENT_EXTRA_NEW_CART_ITEM = "newCartItem"
        const val NEW_CART_ITEM_CODE = 1
        const val INTENT_EXTRA_CART = "cart"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Base_Theme_TemperoDigital)
        super.onCreate(savedInstanceState)

        // Bind the activity layout
        binding = ActivityMenuListBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Activity Result Launchers
        cartActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> onCartResult(result)
        }

        menuItemDetailActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result -> onFullItemResult(result)
        }


        menuList = loadMenuListData()

        // Create the menu List and adapter
        val menuListRecyclerView = binding.menuListRecyclerView
        menuListRecyclerView.adapter = MenuListAdapter(MenuList(menuList.sections), this)
        menuListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        // Set up the toolbar
        val customBar = CustomBar()
        customBar.showCustomToolbar(this, binding.toolbar)
    }
```

No código, observa-se a criação de um RecyclerView que exibe as seções de itens. Cada item desse RecyclerView é, por sua vez, outro RecyclerView. O primeiro RecyclerView utiliza um layout vertical, enquanto os outros dentro dele utilizam um layout horizontal. Além disso, temos a função loadMenuListData, que carrega nosso arquivo MOCK.json, onde estão os itens do menu. Aqui está o código dessa função:

```kotlin
private fun loadMenuListData(): MenuList {
        val file = resources.assets.open("Mock.json")
        val json = file.bufferedReader().use { it.readText() }
        val gson = Gson()

        file.close()

        return gson.fromJson(json, MenuList::class.java)
    }
```

Utilizamos a biblioteca Gson para serialização e desserialização de objetos em JSON. Também definimos um objeto companion com constantes para criar intents que abrirão nossas outras atividades, como a de item e a de carrinho. Além disso, observa-se a criação de uma barra de ferramentas personalizada, exibindo apenas o nome do aplicativo. 

## Tela de Item
![Imagem de item específico](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Tela%20de%20Item%201.png)
Aqui temos a tela do item, que exibe uma descrição do item e botões de adição, entre outros elementos. No botão de adicionar ao pedido, implementamos um tratamento de erros que impede a adição de um item com quantidade zero. Veja o código:

```kotlin
class MenuItemDetail : AppCompatActivity(), MenuItemDetailClickListener {
    private var counter: Int = 0
    private lateinit var binding: ActivityMenuItemDetailBinding
    private lateinit var counterView: TextView
    private lateinit var menuItem: MenuListItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind the activity layout
        binding = ActivityMenuItemDetailBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        menuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(
                MenuListActivity.INTENT_EXTRA_MENU_ITEM, MenuListItem::class.java
            )!!
        } else {
            // Deprecated in SDK 33
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_MENU_ITEM)!!
        }

        counterView = binding.cardCounter.textCounter

        // Show menu item detail
        binding.textMenuItemDetailName.text = menuItem.name
        binding.textMenuItemDetailDesc.text = menuItem.desc
        binding.textMenuItemDetailPrice.text =
            NumberFormat.getCurrencyInstance(Locale.getDefault()).format(menuItem.price)

        val displayMetrics = binding.root.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        Picasso.get().load(menuItem.imageUrl).resize(screenWidth, 0)
            .placeholder(R.drawable.ic_launcher_foreground).into(binding.imageMenuItemDetail)

        eventListenersSet()
    }

    private fun eventListenersSet() {
        binding.cardCounter.buttonMenuItemDetailAdd.setOnClickListener {
            onAddCounterMenuItemClick(counterView)
        }

        binding.cardCounter.buttonMenuItemDetailMinus.setOnClickListener {
            onRemoveCounterMenuItemClick(counterView)
        }

        binding.buttonMenuItemDetailAddToCart.setOnClickListener {
            onAddToCartMenuItemClick(menuItem)
        }

        binding.textEditObservation.addTextChangedListener(onChangeObservationText(binding.textEditObservation))
    }

    override fun onChangeObservationText(item: AppCompatEditText): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                menuItem.observation = s.toString()
            }
        }
    }

    override fun onAddCounterMenuItemClick(item: TextView) {
        counter++
        item.text = counter.toString()
    }

    override fun onRemoveCounterMenuItemClick(item: TextView) {
        if (item.text.toString().toInt() > 0) {
            counter--
            item.text = counter.toString()
        }
    }

    override fun onAddToCartMenuItemClick(menuItem: MenuListItem) {
        if (counter > 0) {
            val newCartItem = MenuListItem(
                menuItem.name,
                menuItem.price,
                menuItem.imageUrl,
                menuItem.desc,
                menuItem.time,
                counterView.text.toString().toInt(),
                menuItem.observation
            )
            val resultIntent = Intent()
            resultIntent.putExtra(MenuListActivity.INTENT_EXTRA_NEW_CART_ITEM, newCartItem)
            setResult(RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Adicione ao menos um item", Toast.LENGTH_SHORT).show()
        }
    }
}
```
Ao final, quando clicamos em "adicionar ao pedido", retornamos à página do cardápio enviando o item adicionado para ser processado lá pela seguinte função:

```
    fun onFullItemResult(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            val newMenuItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(
                    INTENT_EXTRA_NEW_CART_ITEM, MenuListItem::class.java
                )
            } else {
                // Deprecated in SDK 33
                result.data?.getParcelableExtra(INTENT_EXTRA_NEW_CART_ITEM)
            }

            if(newMenuItem == null) {
                return
            }

            val equalElement = cart.items.find { newMenuItem.name == it.name }
            if(equalElement != null) {
                equalElement.quantity += newMenuItem.quantity
                cart.totalPrice = cart.totalPrice + (newMenuItem.price * newMenuItem.quantity)
                return
            }

            cart.items.add(newMenuItem!!)
            cart.totalPrice = cart.totalPrice + (newMenuItem.price * newMenuItem.quantity)
        }
```
Esta função recebe o pacote do intent usando getParcelableExtra e utiliza-o para adicionar à lista do carrinho. Além disso, há um tratamento de dados que verifica se o item já existe na lista, apenas adicionando a nova quantidade ao item existente, se for o caso. Também temos um tratamento que define qual método vai ser utilizado na hora de receber o parcelable, fazendo com que o aplicativo utilize a versão mais nova quando possível, já que ela é mais segura.

## Tela de Cardápio com Itens no Carrinho
![Imagem de item específico](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Tela%20de%20Card%C3%A1pio%20com%20Total.png)
Agora que há itens no nosso carrinho, é possível observar um fragmento que exibe o total a pagar para o cliente, além de apresentar o botão que direciona para a Activity do carrinho, onde a compra pode ser finalizada. Abaixo, o código responsável pelo fragmento:

```kotlin
    private fun showCartCard() {
        if (cart.items.size > 0) {
            var totalPriceBundle = bundleOf("totalPrice" to cart.totalPrice)
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CardCartFragment>(R.id.fragment_card_cart_container, args = totalPriceBundle)
            }
        }
    }

    private fun rebuildFragment() {
        val existingFragment = supportFragmentManager.findFragmentById(R.id.fragment_card_cart_container)

        existingFragment?.let {
            supportFragmentManager.beginTransaction().remove(it).commit()
        }

        showCartCard()
    }
```
Ambas essas funções cuidam da exibição do carrinho, mostrando-o apenas quando houver algum item dentro dele. Se houver itens no carrinho (ou seja, se o tamanho da lista de itens for maior que zero), um bundle é criado com a chave "totalPrice" e o valor correspondente ao preço total do carrinho. Esse pacote é então passado para o fragmento do carrinho. Utilizando o supportFragmentManager a gente invoca o carrinho, utilizando um container de fragment que foi definido lá no nosso layout e passando nossos argumentos.

```
class CardCartFragment : Fragment() {
    lateinit private var binding: FragmentCardCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCardCartBinding.inflate(LayoutInflater.from(context))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val activity = activity as MenuListActivity
        val formatter = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault())
        // Inflate the layout for this fragment
        val textTotalPrice = binding.textCartCardTotal
        val totalPrice = arguments?.getFloat("totalPrice")

        binding.buttonOpenCart.setOnClickListener() {
            activity.launchCart()
        }

        if(totalPrice != null) {
            textTotalPrice.text = formatter.format(totalPrice)
        }

        return binding.root
    }
}
```
Este é o nosso fragmento do carrinho. Ele recebe os argumentos passados pelo método showCartCard para exibir o preço total dos itens no carrinho. O botão "buttonOpenCart" permite ao usuário abrir o carrinho para finalizar a compra.

## Tela de Carrinho
![Imagem da tela do carrinho](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Tela%20de%20Carrinho.png)
Nesta tela, você encontrará o carrinho de compras, onde todos os itens adicionados são exibidos. Aqui, você pode aumentar a quantidade de porções de cada item ou excluí-los diretamente. Um tratamento especial é aplicado: se a quantidade diminuir para zero, o item será automaticamente removido. Além disso, há um cartão exibindo o total de todos os itens para o cliente. Por fim, temos o botão "Finalizar Pedido". É importante ressaltar que, caso o cliente delete todos os itens do carrinho e clique em "Finalizar Pedido", um toast será exibido alertando que não há nada no carrinho.
```
class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartItems: Cart
    private lateinit var counterView: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cartItems = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_CART, Cart::class.java)!!
        } else {
            // Older version
            intent.getParcelableExtra(MenuListActivity.INTENT_EXTRA_CART)!!
        }

        val cartListRecyclerView = binding.recyclerViewCart
        cartListRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        cartListRecyclerView.adapter = CartListAdapter(cartItems, cartItems, updateTotalPrice)

        binding.textTotalPrice.text = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault()).format(cartItems.totalPrice)

        // Set up the toolbar
        val customBar = CustomBar()
        customBar.showCustomToolbar(this, binding.toolbar)

        val callBack = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sendCartData()
            }
        }

        onBackPressedDispatcher.addCallback(this, callBack)

        // Set up Finish Order Button
        binding.buttonCartFinish.setOnClickListener {
            if(cartItems.items.isEmpty()) {
                Toast.makeText(this, "Seu carrinho esta vazio!", Toast.LENGTH_LONG).show()
                return
            }

            cartItems.items.clear()
            Toast.makeText(this, "Seu pedido foi enviado à nossa cozinha!", Toast.LENGTH_LONG).show()
            sendCartData()
        }
    }

    private fun sendCartData() {
        val resultIntent = Intent()
        resultIntent.putExtra(MenuListActivity.INTENT_EXTRA_CART, cartItems)
        setResult(RESULT_OK, resultIntent)
        finish()
    }

    val updateTotalPrice: () -> Unit ={
        binding.textTotalPrice.text = java.text.NumberFormat.getCurrencyInstance(Locale.getDefault()).format(cartItems.totalPrice)
    }
}
```
Este código também inclui uma função para atualizar o preço total quando ocorre qualquer alteração na RecyclerView. Essa função é passada para a RecyclerView e chamada quando houver mudanças.

## Finalizando
![Imagem da tela de cardápio com toast de compra finalizada](https://github.com/eltonls/tempero-digital-kt/blob/main/Presentation/Toast%20de%20Finish.png)
Ao fim de tudo, a tela do carrinho volta para tela de cardápio, mostrando o toast de pedido finalizado com sucesso. 
